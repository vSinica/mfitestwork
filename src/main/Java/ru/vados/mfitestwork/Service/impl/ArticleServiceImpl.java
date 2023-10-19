package ru.vados.mfitestwork.Service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vados.mfitestwork.Dto.ArticleDto;
import ru.vados.mfitestwork.Entity.ArticleEntity;
import ru.vados.mfitestwork.Repository.ArticleRepository;
import ru.vados.mfitestwork.Service.ArticleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    @Transactional
    public void addArticle(ArticleDto item){
        articleRepository.save(convert(item));
    }

    @Override
    @Transactional
    public List<ArticleDto> getArticles(){
        return articleRepository.findAll().stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArticleDto getArticleById(Long id){
        return convert(articleRepository.findByArticleId(id).orElseThrow());
    }

    @Override
    @Transactional
    public List<ArticleDto> getArticlesByNewsSite(String newsSite){
        return articleRepository.findAllByNewsSite(newsSite).stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }




    ArticleEntity convert(ArticleDto item){
        ArticleEntity entity = new ArticleEntity();
        entity.setArticleId(item.getArticleId());
        entity.setTitle(item.getTitle());
        entity.setNewsSite(item.getNewsSite());
        entity.setPublishedDate(item.getPublishedDate());
        entity.setArticle(item.getArticle());
        return entity;
    }

    ArticleDto convert(ArticleEntity entity){
        return new ArticleDto(entity.getArticleId(), entity.getTitle(),
                entity.getNewsSite(), entity.getPublishedDate(), entity.getArticle());
    }
}
