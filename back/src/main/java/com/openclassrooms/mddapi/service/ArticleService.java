package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.input.ArticleInputDto;
import com.openclassrooms.mddapi.dto.response.ArticleResponseDto;

import java.util.List;

public interface ArticleService {

    void saveArticle(ArticleInputDto articleInputDto);

    List<ArticleResponseDto> getArticlesByUser(String login);
}
