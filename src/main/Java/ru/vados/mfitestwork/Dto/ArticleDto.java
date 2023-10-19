package ru.vados.mfitestwork.Dto;

import lombok.Value;

import java.time.Instant;

@Value
public class ArticleDto {
     Long articleId;
     String title;
     String newsSite;
     Instant publishedDate;
     String article;
}
