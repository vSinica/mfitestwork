package ru.vados.mfitestwork;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.vados.mfitestwork.Dto.ArticleDto;
import ru.vados.mfitestwork.Service.ArticleService;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ArticleFetch {
    @Value("${fetch-param.thread-pool}")
    private int threadPool;
    @Value("${fetch-param.article-thread-limit}")
    private Integer articleThreadLimit;
    @Value("${fetch-param.buffer-limit}")
    private Integer bufferLimit;
    @Value("${fetch-param.total-article-limit}")
    private Integer totalArtclelimit;
    @Value("${fetch-param.url}")
    private String url;
    @Value("${fetch-param.black-list-name}")
    private String blackListName;

    private List<String> blackList;

    private final CloseableHttpClient httpClient;
    private final ArticleService articleService;
    private final ObjectMapper mapper;
    private final BlackListReader blackListReader;
    private final Object lockObj = new Object();

    private final Map<String, List<ArticleReference>> buffer = new ConcurrentHashMap<>();

    public ArticleFetch(CloseableHttpClient httpClient, ArticleService articleService, ObjectMapper mapper, BlackListReader blackListReader) {
        this.httpClient = httpClient;
        this.articleService = articleService;
        this.mapper = mapper;
        this.blackListReader = blackListReader;
    }

    @AllArgsConstructor
    public class ClientThread extends Thread{

        private final CloseableHttpClient httpClient;
        private final String url;
        private final Integer start;
        private final Integer limit;
        private final ArticleFetch articleFetch;

        @Override
        public void run() {
            try{
                HttpGet httpGet = new HttpGet(url);
                URI uri = new URIBuilder(httpGet.getUri())
                        .addParameter("_limit", limit.toString())
                        .addParameter("_start", start.toString())
                        .build();
                httpGet.setUri(uri);

                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity entity = httpResponse.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                List<ArticleReference> articleReferences = mapper.readValue(responseString, new TypeReference<>() {});

                articleReferences.stream()
                        .filter(a -> blackList.stream().noneMatch(b -> a.getTitle().toLowerCase().contains(b.toLowerCase())))
                        .sorted(Comparator.comparing(ArticleReference::getPublishedAt))
                        .forEach(c -> buffer.computeIfAbsent(c.getNewsSite(), k -> new ArrayList<>()).add(c));

                for (Map.Entry<String, List<ArticleReference>> bufferEntry : buffer.entrySet()) {
                    if (bufferEntry.getValue().size() >= bufferLimit) {
                        synchronized (lockObj) {
                            bufferEntry.getValue().forEach(articleFetch::saveArticleBody);
                            buffer.remove(bufferEntry.getKey());
                        }
                    }
                }
            }catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fetch() {
        blackList = blackListReader.read(blackListName);

        int articleStartCount = 0;
        List<ClientThread> threads = new ArrayList<>();
        while(articleStartCount < totalArtclelimit){

            threads.clear();
            for(int i=0;i<threadPool;i++){
                if(articleStartCount <= totalArtclelimit) {
                    threads.add(new ClientThread(httpClient, url, articleStartCount, articleThreadLimit, this));
                    articleStartCount += articleThreadLimit;
                }
                else {
                    break;
                }
            }
            threads.stream()
                    .peek(Thread::start)
                    .forEach(a-> {
                        try {
                            a.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        for (Map.Entry<String, List<ArticleReference>> bufferEntry : buffer.entrySet()) {
            bufferEntry.getValue().forEach(this::saveArticleBody);
            buffer.remove(bufferEntry.getKey());
        }
    }

    public void saveArticleBody(ArticleReference ref){

        HttpGet request = new HttpGet(ref.getUrl());
        CloseableHttpResponse response = null;
        String responseString = null;
        try {
            response = httpClient.execute(request);
            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        Document fullPage = Jsoup.parse(responseString);
        String onlyText = fullPage.body().text();

        ArticleDto item = new ArticleDto(ref.getId(), ref.getTitle(), ref.getNewsSite(), ref.getPublishedAt(), onlyText);

        articleService.addArticle(item);
    }

}
