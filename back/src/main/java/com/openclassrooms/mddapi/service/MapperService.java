package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.response.ArticleResponseDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.dto.response.TopicDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Topic;

public interface MapperService {

    ArticleResponseDto constructArticleResponse(Article article);

    CommentResponseDto constructCommentResponse(Comment comment);

    TopicDto constructTopicResponse(Topic topic);
}
