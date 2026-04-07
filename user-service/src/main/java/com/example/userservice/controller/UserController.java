package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 
 * RESTful API 设计规范
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * 创建用户
     * POST /api/v1/users
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("创建用户请求: {}", userDTO.getUsername());
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * 根据ID查询用户
     * GET /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable @Min(1) Long id) {
        log.info("查询用户请求: id={}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 根据用户名查询用户
     * GET /api/v1/users/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(
            @PathVariable String username) {
        log.info("查询用户请求: username={}", username);
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * 分页查询用户列表
     * GET /api/v1/users?page=0&size=10&sort=id,desc
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {
        
        log.info("查询用户列表: page={}, size={}, keyword={}", page, size, keyword);
        
        // 解析排序参数
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<UserDTO> userPage = userService.listUsers(keyword, status, pageable);
        return ResponseEntity.ok(userPage);
    }

    /**
     * 更新用户信息
     * PUT /api/v1/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody UserDTO userDTO) {
        log.info("更新用户请求: id={}", id);
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 删除用户
     * DELETE /api/v1/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Min(1) Long id) {
        log.info("删除用户请求: id={}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 更新用户状态
     * PATCH /api/v1/users/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable @Min(1) Long id,
            @RequestParam Integer status) {
        log.info("更新用户状态: id={}, status={}", id, status);
        userService.updateUserStatus(id, status);
        return ResponseEntity.ok().build();
    }

    /**
     * 健康检查
     * GET /api/v1/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "user-service");
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}