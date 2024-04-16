package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.input.CommentInputDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {

    void saveComment(CommentInputDto commentInputDto);

    List<CommentResponseDto> getCommentsByArticle(long articleId);
}
