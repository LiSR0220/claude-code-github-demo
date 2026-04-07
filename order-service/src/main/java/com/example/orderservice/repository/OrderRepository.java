package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 订单数据访问层
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据订单号查询
     */
    Optional<Order> findByOrderNo(String orderNo);

    /**
     * 根据用户ID分页查询订单
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据状态分页查询订单
     */
    Page<Order> findByStatus(Integer status, Pageable pageable);

    /**
     * 搜索订单（订单号或商品名）
     */
    @Query("SELECT o FROM Order o WHERE " +
           "(:keyword IS NULL OR o.orderNo LIKE %:keyword% OR o.productName LIKE %:keyword%) AND " +
           "(:userId IS NULL OR o.userId = :userId) AND " +
           "(:status IS NULL OR o.status = :status)")
    Page<Order> searchOrders(@Param("keyword") String keyword,
                             @Param("userId") Long userId,
                             @Param("status") Integer status,
                             Pageable pageable);
}