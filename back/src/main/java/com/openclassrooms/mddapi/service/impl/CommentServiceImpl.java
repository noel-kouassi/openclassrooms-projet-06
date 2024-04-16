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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.openclassrooms.mddapi.util.DateFormatter.formatDate;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
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
        return comments.stream().map(comment -> constructComment(comment, mapUserByName, mapUserByEmail)).toList();
    }

    private CommentResponseDto constructComment(Comment comment, Map<String, User> mapUserByName, Map<String, User> mapUserByEmail) {

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
