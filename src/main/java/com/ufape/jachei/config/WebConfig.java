package com.ufape.jachei.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeia as requisições HTTP para a pasta física no servidor
        // O "file:uploads/" refere-se a uma pasta chamada "uploads" na mesma raiz de onde o Java está rodando
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}