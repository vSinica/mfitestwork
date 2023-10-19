package ru.vados.mfitestwork.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.vados.mfitestwork.BlackListReader;


@Configuration
@ComponentScan(basePackages = {"ru.vados.mfitestwork"})
@EnableTransactionManagement
@EnableAsync
@Import({ConfigProperties.class, ServiceConfig.class})
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        return mapper;
    }

    @Bean
    public CloseableHttpClient httpClient(){
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(100);
        HttpClientBuilder clientbuilder = HttpClients.custom().setConnectionManager(connManager);
        return clientbuilder.build();
    }

    @Bean
    public BlackListReader blackListReader(){
        return new BlackListReader();
    }

}
