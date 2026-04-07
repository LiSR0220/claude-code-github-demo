package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 认证过滤器
 * 用于需要登录验证的路由
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            
            log.info("[认证过滤器] 请求路径: {}, 是否需要认证: {}", 
                    path, config.isRequired());
            
            // 如果不需要认证，直接放行
            if (!config.isRequired()) {
                return chain.filter(exchange);
            }
            
            // 检查 Token
            String token = request.getHeaders().getFirst(TOKEN_HEADER);
            
            if (token == null || !token.startsWith(TOKEN_PREFIX)) {
                log.warn("[认证失败] 缺少或格式错误的 Token: {}", path);
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            
            // TODO: 实际项目中需要验证 Token 的有效性
            // 这里简单示例，只检查格式
            String realToken = token.substring(TOKEN_PREFIX.length());
            if (realToken.isEmpty()) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            
            log.info("[认证成功] Token 验证通过");
            
            // 可以在这里添加用户信息到请求头，传递给下游服务
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", "user123")
                    .build();
            
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("required");
    }

    /**
     * 配置类
     */
    public static class Config {
        private boolean required = true;

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }
    }
}