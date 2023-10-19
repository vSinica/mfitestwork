package ru.vados.mfitestwork.Config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"ru.vados.mfitestwork.Controller", "ru.vados.mfitestwork.Service"})
@EnableJpaRepositories(basePackages = "ru.vados.mfitestwork.Repository")
@EntityScan(basePackages = {"ru.vados.mfitestwork.Entity"})
public class ServiceConfig {

}
