package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
