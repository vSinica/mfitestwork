package ru.vados.mfitestwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = "ru.vados.mfitestwork")
@EnableWebMvc
public class App {
    public static void main(String[] args) {SpringApplication.run(App.class, args);}
}
