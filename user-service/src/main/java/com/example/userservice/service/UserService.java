package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
public interface UserService {

    /**
     * 创建用户
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * 根据ID查询用户
     */
    UserDTO getUserById(Long id);

    /**
     * 根据用户名查询用户
     */
    UserDTO getUserByUsername(String username);

    /**
     * 分页查询用户列表
     */
    Page<UserDTO> listUsers(String keyword, Integer status, Pageable pageable);

    /**
     * 更新用户信息
     */
    UserDTO updateUser(Long id, UserDTO userDTO);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 启用/禁用用户
     */
    void updateUserStatus(Long id, Integer status);
}