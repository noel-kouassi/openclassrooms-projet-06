package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.input.ArticleInputDto;
import com.openclassrooms.mddapi.dto.response.ArticleResponseDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.openclassrooms.mddapi.util.DateFormatter.formatDate;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final TopicRepository topicRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, TopicRepository topicRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveArticle(ArticleInputDto articleInputDto) {

        Topic topic = topicRepository.findById(articleInputDto.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(("Topic with id %s not found"), articleInputDto.getTopicId())));

        Article article = new Article();
        article.setTitle(articleInputDto.getTitle());
        article.setDescription(articleInputDto.getDescription());
        article.setTopic(topic);
        articleRepository.save(article);
    }

    @Override
    public List<ArticleResponseDto> getArticlesByUser(String login) {

        List<User> users = userRepository.findAll();
        Map<String, User> mapUserByName = users.stream().collect(toMap(User::getName, identity()));
        Map<String, User> mapUserByEmail = users.stream().collect(toMap(User::getEmail, identity()));

        User user = userRepository.findByEmailOrName(login, login)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with login %s", login)));
        Set<Topic> topics = user.getTopics();

        List<Article> articles = topics.stream().flatMap(topic -> topic.getArticles().stream()).toList();
        return articles.stream().map(article -> constructArticleResponse(article, mapUserByName, mapUserByEmail)).toList();
    }

    private ArticleResponseDto constructArticleResponse(Article article, Map<String, User> mapUserByName, Map<String, User> mapUserByEmail) {

        User user = userRepository.findByEmailOrName(article.getCreatedBy(), article.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with login %s", article.getCreatedBy())));

        ArticleResponseDto articleResponseDto = modelMapper.map(article, ArticleResponseDto.class);
        String topic = article.getTopicTitle();

        articleResponseDto.setTopic(topic);
        articleResponseDto.setAuthor(user.getName());
        articleResponseDto.setCreationDate(formatDate(article.getCreatedAt()));

        List<Comment> comments = article.getComments();
        if (!comments.isEmpty()) {
            List<CommentResponseDto> commentResponseDtos = comments.stream().map(comment -> {
                CommentResponseDto commentResponseDto = new CommentResponseDto();
                commentResponseDto.setDescription(comment.getDescription());
                commentResponseDto.setCreationDate(formatDate(comment.getCreatedAt()));

                String authorLogin = comment.getCreatedBy();
                if (mapUserByName.get(authorLogin) != null) {
                    commentResponseDto.setAuthor(mapUserByName.get(authorLogin).getName());
                }
                if (mapUserByEmail.get(authorLogin) != null) {
                    commentResponseDto.setAuthor(mapUserByEmail.get(authorLogin).getName());
                }
                return commentResponseDto;
            }).toList();
            articleResponseDto.setComments(commentResponseDtos);
        }
        return articleResponseDto;
    }
}
