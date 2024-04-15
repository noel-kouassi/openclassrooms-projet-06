package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.input.ArticleInputDto;
import com.openclassrooms.mddapi.dto.response.ArticleResponseDto;
import com.openclassrooms.mddapi.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * @param articleInputDto input data which contains the article information for creation
     * @return String as message response
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createArticle(@Valid @RequestBody ArticleInputDto articleInputDto) {
        articleService.saveArticle(articleInputDto);
        return new ResponseEntity<>("Article created successfully !", HttpStatus.OK);
    }

    /**
     * @param login name or email of the user to be used for article recuperation
     * @return List of ArticleResponseDto as message response
     */
    @GetMapping("/articles/{login}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ArticleResponseDto>> getArticlesByUser(@PathVariable String login) {
        List<ArticleResponseDto> articleResponseDtos = articleService.getArticlesByUser(login);
        return new ResponseEntity<>(articleResponseDtos, HttpStatus.OK);
    }
}
