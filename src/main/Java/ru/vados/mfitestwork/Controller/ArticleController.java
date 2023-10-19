package ru.vados.mfitestwork.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vados.mfitestwork.Dto.ArticleDto;
import ru.vados.mfitestwork.Service.ArticleService;

import java.util.List;

@RestController
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("getArticles")
    public List<ArticleDto> getArticles(){
        return articleService.getArticles();
    }

    @GetMapping("getArticlesById/{id}")
    public ArticleDto getArticleById(@PathVariable Long id){
        return articleService.getArticleById(id);
    }

    @GetMapping("getArticlesBySite/{newsSite}")
    public List<ArticleDto> getAtriclesByNewsSite(@PathVariable String newsSite){
        return articleService.getArticlesByNewsSite(newsSite);
    }
}
