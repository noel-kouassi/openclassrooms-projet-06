package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.input.ArticleInputDto;
import com.openclassrooms.mddapi.dto.response.ArticleResponseDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final CommonService commonService;

    private final ArticleRepository articleRepository;

    private final TopicRepository topicRepository;

    private final UserRepository userRepository;

    @Autowired
    public ArticleServiceImpl(CommonService commonService, ArticleRepository articleRepository, TopicRepository topicRepository, UserRepository userRepository) {
        this.commonService = commonService;
        this.articleRepository = articleRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
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

        User user = commonService.findUserByLogin(login, mapUserByName, mapUserByEmail);
        Set<Topic> topics = user.getTopics();

        List<Article> articles = topics.stream().flatMap(topic -> topic.getArticles().stream()).toList();
        return articles.stream().map(article -> commonService.constructArticleResponse(article, mapUserByName, mapUserByEmail)).toList();
    }

}
