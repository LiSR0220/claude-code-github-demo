package com.example.userservice.service.impl;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * 用户服务实现类
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("用户名已存在: " + userDTO.getUsername());
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        
        // TODO: 密码需要加密存储
        user.setPassword("default_password");
        
        User savedUser = userRepository.save(user);
        log.info("用户创建成功: {}", savedUser.getUsername());
        
        return convertToDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在: " + id));
        return convertToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在: " + username));
        return convertToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> listUsers(String keyword, Integer status, Pageable pageable) {
        Page<User> userPage = userRepository.findByKeywordAndStatus(keyword, status, pageable);
        return userPage.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在: " + id));

        // 更新允许修改的字段
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setNickName(userDTO.getNickName());
        user.setAvatarUrl(userDTO.getAvatarUrl());

        User updatedUser = userRepository.save(user);
        log.info("用户更新成功: {}", updatedUser.getUsername());
        
        return convertToDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("用户不存在: " + id);
        }
        userRepository.deleteById(id);
        log.info("用户删除成功: {}", id);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long id, Integer status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在: " + id));
        user.setStatus(status);
        userRepository.save(user);
        log.info("用户状态更新成功: {}, status={}", id, status);
    }

    /**
     * 实体转DTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}