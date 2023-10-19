package ru.vados.mfitestwork;

import lombok.Data;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
public class ArticleReference {
    Long id;
    String title;
    String url;
    String imageUrl;
    String newsSite;
    String summary;
    Instant publishedAt;
    Instant updatedAt;
    Boolean featured;
    List<Launches> launches;
    List<Events> events;
}
