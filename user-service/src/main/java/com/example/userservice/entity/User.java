package com.example.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
    @Column(nullable = false, length = 100)
    private String password;

    @Email(message = "邮箱格式不正确")
    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "nick_name", length = 50)
    private String nickName;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "status", nullable = false)
    private Integer status = 1; // 1: 正常, 0: 禁用

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}