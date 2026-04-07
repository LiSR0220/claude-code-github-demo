package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 分页查询用户（带搜索条件）
     */
    @Query("SELECT u FROM User u WHERE " +
           "(:keyword IS NULL OR u.username LIKE %:keyword% OR u.nickName LIKE %:keyword%) AND " +
           "(:status IS NULL OR u.status = :status)")
    Page<User> findByKeywordAndStatus(@Param("keyword") String keyword,
                                       @Param("status") Integer status,
                                       Pageable pageable);
}