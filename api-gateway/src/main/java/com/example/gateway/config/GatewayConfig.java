package com.example.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * 网关配置类
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Slf4j
@Configuration
public class GatewayConfig {

    /**
     * 全局日志过滤器
     * 记录所有请求的时间戳
     */
    @Bean
    @Order(-1)
    public GlobalFilter loggingFilter() {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethodValue();
            
            log.info("[网关请求] {} {} - 时间: {}", 
                    method, path, new Date());
            
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpStatus statusCode = exchange.getResponse().getStatusCode();
                log.info("[网关响应] {} {} - 状态: {}", 
                        method, path, statusCode);
            }));
        };
    }

    /**
     * 请求头添加过滤器
     * 为每个请求添加网关标识
     */
    @Bean
    @Order(0)
    public GlobalFilter addHeaderFilter() {
        return (exchange, chain) -> {
            exchange.getRequest().mutate()
                    .header("X-Gateway-Service", "api-gateway")
                    .header("X-Request-Time", String.valueOf(System.currentTimeMillis()))
                    .build();
            
            return chain.filter(exchange);
        };
    }
}