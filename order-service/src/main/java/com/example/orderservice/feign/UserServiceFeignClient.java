package com.example.orderservice.feign;

import com.example.orderservice.dto.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务 Feign 客户端
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@FeignClient(
        name = "user-service",
        fallbackFactory = UserServiceFeignClient.UserServiceFallbackFactory.class
)
public interface UserServiceFeignClient {

    /**
     * 根据ID查询用户信息
     */
    @GetMapping("/api/v1/users/{id}")
    UserInfoDTO getUserById(@PathVariable("id") Long id);

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/v1/users/username/{username}")
    UserInfoDTO getUserByUsername(@PathVariable("username") String username);

    // ==================== 降级处理类 ====================
    
    @Slf4j
    @Component
    class UserServiceFallbackFactory implements feign.hystrix.FallbackFactory<UserServiceFeignClient> {
        
        @Override
        public UserServiceFeignClient create(Throwable cause) {
            log.error("用户服务调用失败，触发降级", cause);
            
            return new UserServiceFeignClient() {
                @Override
                public UserInfoDTO getUserById(Long id) {
                    log.warn("降级处理: getUserById({})", id);
                    return UserInfoDTO.builder()
                            .id(id)
                            .username("未知用户")
                            .nickName("服务暂时不可用")
                            .build();
                }

                @Override
                public UserInfoDTO getUserByUsername(String username) {
                    log.warn("降级处理: getUserByUsername({})", username);
                    return UserInfoDTO.builder()
                            .username(username)
                            .nickName("服务暂时不可用")
                            .build();
                }
            };
        }
    }
}