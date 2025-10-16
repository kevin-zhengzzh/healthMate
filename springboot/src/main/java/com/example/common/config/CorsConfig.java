package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1 set origin address
        corsConfiguration.addAllowedHeader("*"); // 2 set request head
        corsConfiguration.addAllowedMethod("*"); // 3 set request method
        source.registerCorsConfiguration("/**", corsConfiguration); // 4 config cors for interface
        return new CorsFilter(source);
    }
}