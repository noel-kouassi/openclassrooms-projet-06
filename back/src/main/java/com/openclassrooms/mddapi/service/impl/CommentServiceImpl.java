package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.input.CommentInputDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommonService commonService;

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommonService commonService, CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.commonService = commonService;
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
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

        List<User> users = userRepository.findAll();
        Map<String, User> mapUserByName = users.stream().collect(toMap(User::getName, identity()));
        Map<String, User> mapUserByEmail = users.stream().collect(toMap(User::getEmail, identity()));

        List<Comment> comments = article.getComments();
        return comments.stream().map(comment -> commonService.constructComment(comment, mapUserByName, mapUserByEmail)).toList();
    }
}
