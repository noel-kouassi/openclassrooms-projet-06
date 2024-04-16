package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.response.ArticleResponseDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.User;

import java.util.Map;

public interface CommonService {

    ArticleResponseDto constructArticleResponse(Article article, Map<String, User> mapUserByName, Map<String, User> mapUserByEmail);

    CommentResponseDto constructComment(Comment comment, Map<String, User> mapUserByName, Map<String, User> mapUserByEmail);

    User findUserByLogin(String login, Map<String, User> mapUserByName, Map<String, User> mapUserByEmail);
}
