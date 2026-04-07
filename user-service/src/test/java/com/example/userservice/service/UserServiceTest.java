package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试类
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .username("testuser" + System.currentTimeMillis())
                .email("test@example.com")
                .nickName("Test User")
                .build();

        // when
        UserDTO created = userService.createUser(userDTO);

        // then
        assertNotNull(created.getId());
        assertEquals(userDTO.getUsername(), created.getUsername());
        assertEquals(userDTO.getEmail(), created.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUsernameExists() {
        // given
        String username = "duplicateuser" + System.currentTimeMillis();
        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .email("test@example.com")
                .build();
        userService.createUser(userDTO);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userDTO);
        });
    }
}