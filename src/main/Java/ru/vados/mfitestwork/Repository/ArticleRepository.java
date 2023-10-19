package ru.vados.mfitestwork.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vados.mfitestwork.Entity.ArticleEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    List<ArticleEntity> findAllByNewsSite(String newsSite);

    Optional<ArticleEntity> findByArticleId(Long articleId);
}
