package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.input.CommentInputDto;
import com.openclassrooms.mddapi.dto.response.CommentResponseDto;
import com.openclassrooms.mddapi.service.CommentService;
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
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * @param commentInputDto input data which contains the comment information for creation
     * @return String as message response
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createComment(@Valid @RequestBody CommentInputDto commentInputDto) {
        commentService.saveComment(commentInputDto);
        return new ResponseEntity<>("Comment created successfully !", HttpStatus.OK);
    }

    /**
     * @param articleId the id of the article to be used for comment retrievement
     * @return List of commentResponseDto as response
     */
    @GetMapping("/{articleId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByArticle(@PathVariable long articleId) {
        List<CommentResponseDto> commentResponseDtos = commentService.getCommentsByArticle(articleId);
        return new ResponseEntity<>(commentResponseDtos, HttpStatus.OK);
    }
}
