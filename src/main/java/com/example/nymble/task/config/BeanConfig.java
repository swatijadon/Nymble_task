package com.example.nymble.task.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class BeanConfig {
    @Bean
    public FilterRegistrationBean filterr() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.addAllowedOriginPattern("*");
        cors.addAllowedHeader("Authorization");
        cors.addAllowedHeader("Content-type");
        cors.addAllowedHeader("accept");
        cors.addAllowedMethod("POST");
        cors.addAllowedMethod("GET");
        cors.addAllowedMethod("POST");
        cors.addAllowedMethod("UPDATE");
        cors.addAllowedMethod("application/json");
        cors.setMaxAge(3600l);


        source.registerCorsConfiguration("/**", cors);
        return new FilterRegistrationBean<>(new CorsFilter(source));

    }
}
