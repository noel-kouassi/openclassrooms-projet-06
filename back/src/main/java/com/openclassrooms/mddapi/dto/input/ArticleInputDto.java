package com.openclassrooms.mddapi.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleInputDto {

    private long topicId;

    @NotBlank(message = "Title should not be null or empty")
    private String title;

    @NotBlank(message = "Description should not be null or empty")
    private String description;
}
