package com.openclassrooms.mddapi.entity;

import com.openclassrooms.mddapi.common.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity {

    private String description;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
