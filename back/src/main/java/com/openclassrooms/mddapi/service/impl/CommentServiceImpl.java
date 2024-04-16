package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.input.CommentInputDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final MapperService mapperService;

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    @Autowired
    public CommentServiceImpl(MapperService mapperService, CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.mapperService = mapperService;
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void saveComment(CommentInputDto commentInputDto) {
        Article article = articleRepository.findById(commentInputDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No article found with id %s", commentInputDto.getArticleId())));
        Comment comment = new Comment();
        comment.setDescription(commentInputDto.getDescription());
        comment.setArticle(article);
        commentRepository.save(comment);
    }

    @Override
    public List<CommentResponseDto> getCommentsByArticle(long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No article found with id %s", articleId)));
        List<Comment> comments = article.getComments();
        return comments.stream().map(mapperService::constructCommentResponse).toList();
    }
}
