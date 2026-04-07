package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 用户数据传输对象
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    private String username;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;

    private String nickName;

    private String avatarUrl;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}