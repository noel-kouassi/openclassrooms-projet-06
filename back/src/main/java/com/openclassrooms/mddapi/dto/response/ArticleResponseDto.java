package com.openclassrooms.mddapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDto {

    private long articleId;

    private String title;

    private String description;

    private String topic;

    private String author;

    private String creationDate;

    private List<CommentResponseDto> comments;
}
