package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.response.ArticleResponseDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.service.CommonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.openclassrooms.mddapi.util.DateFormatter.formatDate;

@Service
public class CommonServiceImpl implements CommonService {

    private final ModelMapper modelMapper;

    @Autowired
    public CommonServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ArticleResponseDto constructArticleResponse(Article article, Map<String, User> mapUserByName, Map<String, User> mapUserByEmail) {

        User user = findUserByLogin(article.getCreatedBy(), mapUserByName, mapUserByEmail);

        ArticleResponseDto articleResponseDto = modelMapper.map(article, ArticleResponseDto.class);
        String topic = article.getTopicTitle();

        articleResponseDto.setTopic(topic);
        articleResponseDto.setAuthor(user.getName());
        articleResponseDto.setCreationDate(formatDate(article.getCreatedAt()));

        List<Comment> comments = article.getComments();
        if (!comments.isEmpty()) {
            List<CommentResponseDto> commentResponseDtos = comments.stream().map(comment -> getCommentResponseDto(comment, mapUserByName, mapUserByEmail)).toList();
            articleResponseDto.setComments(commentResponseDtos);
        }
        return articleResponseDto;
    }

    @Override
    public CommentResponseDto constructComment(Comment comment, Map<String, User> mapUserByName, Map<String, User> mapUserByEmail) {
        return getCommentResponseDto(comment, mapUserByName, mapUserByEmail);
    }

    @Override
    public User findUserByLogin(String login, Map<String, User> mapUserByName, Map<String, User> mapUserByEmail) {

        if (mapUserByName.get(login) != null) {
            return mapUserByName.get(login);
        }
        if (mapUserByEmail.get(login) != null) {
            return mapUserByEmail.get(login);
        }
        throw new ResourceNotFoundException(String.format("User not found with login %s", login));
    }

    private CommentResponseDto getCommentResponseDto(Comment comment, Map<String, User> mapUserByName, Map<String, User> mapUserByEmail) {
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
    }
}
