package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地限流过滤器（基于内存）
 * 生产环境建议使用 Redis
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Slf4j
@Component
public class RateLimitFilter extends AbstractGatewayFilterFactory<RateLimitFilter.Config> {

    // 请求计数器 Map<IP, 计数器>
    private static final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    
    // 限流窗口时间（毫秒）
    private static final long TIME_WINDOW = 60 * 1000; // 1分钟

    public RateLimitFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            String path = exchange.getRequest().getURI().getPath();
            
            // 检查是否超过限流阈值
            AtomicInteger counter = requestCounts.computeIfAbsent(clientIp, k -> new AtomicInteger(0));
            int count = counter.incrementAndGet();
            
            if (count > config.getMaxRequests()) {
                log.warn("[限流触发] IP: {}, 路径: {}, 请求数: {}/{}"
                        , clientIp, path, count, config.getMaxRequests());
                
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                
                String msg = "{\"code\":429,\"message\":\"请求过于频繁，请稍后重试\"}";
                DataBuffer buffer = response.bufferFactory().wrap(msg.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            }
            
            log.debug("[限流检查] IP: {}, 请求数: {}/{}", 
                    clientIp, count, config.getMaxRequests());
            
            // 定时清理计数器（简化版，实际用 Redis 过期时间）
            if (count == 1) {
                resetCounterAfterWindow(clientIp);
            }
            
            return chain.filter(exchange);
        };
    }

    /**
     * 窗口时间后重置计数器
     */
    private void resetCounterAfterWindow(String clientIp) {
        new Thread(() -> {
            try {
                Thread.sleep(TIME_WINDOW);
                requestCounts.remove(clientIp);
                log.debug("[限流重置] IP: {} 计数器已重置", clientIp);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * 配置类
     */
    public static class Config {
        // 每分钟最大请求数
        private int maxRequests = 100;

        public int getMaxRequests() {
            return maxRequests;
        }

        public void setMaxRequests(int maxRequests) {
            this.maxRequests = maxRequests;
        }
    }
}