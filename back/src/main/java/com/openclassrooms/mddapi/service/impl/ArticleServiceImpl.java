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
import java.util.Set;

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
        article.setDescription(article.getDescription());
        article.setTopic(topic);
        articleRepository.save(article);
    }

    @Override
    public List<ArticleResponseDto> getArticlesByUser(String login) {

        User user = userRepository.findByEmailOrName(login, login)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with login %s", login)));
        Set<Topic> topics = user.getTopics();
        List<Article> articles = topics.stream().flatMap(topic -> topic.getArticles().stream()).toList();
        return articles.stream().map(this::constructArticleResponse).toList();
    }

    private ArticleResponseDto constructArticleResponse(Article article) {

        ArticleResponseDto articleResponseDto = modelMapper.map(article, ArticleResponseDto.class);
        List<Comment> comments = article.getComments();
        if (!comments.isEmpty()) {
            List<CommentResponseDto> commentResponseDtos = comments.stream().map(comment -> modelMapper.map(comment, CommentResponseDto.class)).toList();
            articleResponseDto.setComments(commentResponseDtos);
        }
        return articleResponseDto;
    }
}
