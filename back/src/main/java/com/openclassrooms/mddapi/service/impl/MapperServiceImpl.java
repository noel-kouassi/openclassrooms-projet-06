package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.response.ArticleResponseDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.dto.response.TopicDto;
import com.openclassrooms.mddapi.dto.response.UserDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.service.MapperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.openclassrooms.mddapi.util.DateFormatter.formatDate;
import static java.util.stream.Collectors.toSet;

@Service
public class MapperServiceImpl implements MapperService {

    private final ModelMapper modelMapper;

    @Autowired
    public MapperServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ArticleResponseDto constructArticleResponse(Article article) {

        ArticleResponseDto articleResponseDto = modelMapper.map(article, ArticleResponseDto.class);
        String topic = article.getTopicTitle();

        articleResponseDto.setTopic(topic);
        articleResponseDto.setAuthor(article.getCreatedBy());
        articleResponseDto.setCreationDate(formatDate(article.getCreatedAt()));

        List<Comment> comments = article.getComments();
        if (!comments.isEmpty()) {
            List<CommentResponseDto> commentResponseDtos = comments.stream().map(this::constructCommentResponse).toList();
            articleResponseDto.setComments(commentResponseDtos);
        }
        return articleResponseDto;
    }

    @Override
    public CommentResponseDto constructCommentResponse(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setDescription(comment.getDescription());
        commentResponseDto.setCreationDate(formatDate(comment.getCreatedAt()));
        commentResponseDto.setAuthor(comment.getCreatedBy());
        return commentResponseDto;
    }

    @Override
    public TopicDto constructTopicResponse(Topic topic) {
        TopicDto topicDto = modelMapper.map(topic, TopicDto.class);
        topicDto.setTopidId(topic.getId());
        Set<User> users = topic.getUsers();
        if (!users.isEmpty()) {
            Set<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(toSet());
            topicDto.setUsers(userDtos);
        }
        return topicDto;
    }
}
