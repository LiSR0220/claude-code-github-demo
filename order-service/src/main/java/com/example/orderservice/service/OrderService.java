package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 订单服务接口
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
public interface OrderService {

    /**
     * 创建订单
     */
    OrderDTO createOrder(OrderDTO orderDTO);

    /**
     * 根据ID查询订单（包含用户信息）
     */
    OrderDTO getOrderById(Long id);

    /**
     * 根据订单号查询
     */
    OrderDTO getOrderByOrderNo(String orderNo);

    /**
     * 分页查询用户订单（包含用户信息）
     */
    Page<OrderDTO> listUserOrders(Long userId, Pageable pageable);

    /**
     * 搜索订单
     */
    Page<OrderDTO> searchOrders(String keyword, Long userId, Integer status, Pageable pageable);

    /**
     * 更新订单状态
     */
    OrderDTO updateOrderStatus(Long id, Integer status);

    /**
     * 取消订单
     */
    void cancelOrder(Long id);
}