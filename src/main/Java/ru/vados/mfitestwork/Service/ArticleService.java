package ru.vados.mfitestwork.Service;

import ru.vados.mfitestwork.Dto.ArticleDto;

import java.util.List;

public interface ArticleService {
    void addArticle(ArticleDto item);

    List<ArticleDto> getArticles();

    ArticleDto getArticleById(Long id);
    List<ArticleDto> getArticlesByNewsSite(String newsSite);

}
