package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.UserInfoDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.feign.UserServiceFeignClient;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * 订单服务实现类
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserServiceFeignClient userServiceFeignClient;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // 验证用户信息（通过 Feign 调用用户服务）
        UserInfoDTO userInfo = userServiceFeignClient.getUserById(orderDTO.getUserId());
        log.info("创建订单，用户信息: {}", userInfo.getUsername());

        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        order.setStatus(0); // 待支付状态

        Order savedOrder = orderRepository.save(order);
        log.info("订单创建成功: {}", savedOrder.getOrderNo());

        return convertToDTO(savedOrder, userInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("订单不存在: " + id));
        
        // 通过 Feign 获取用户信息
        UserInfoDTO userInfo = userServiceFeignClient.getUserById(order.getUserId());
        
        return convertToDTO(order, userInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByOrderNo(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new EntityNotFoundException("订单不存在: " + orderNo));
        
        UserInfoDTO userInfo = userServiceFeignClient.getUserById(order.getUserId());
        
        return convertToDTO(order, userInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> listUserOrders(Long userId, Pageable pageable) {
        // 获取用户信息（缓存优化点）
        UserInfoDTO userInfo = userServiceFeignClient.getUserById(userId);
        
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);
        return orderPage.map(order -> convertToDTO(order, userInfo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> searchOrders(String keyword, Long userId, Integer status, Pageable pageable) {
        Page<Order> orderPage = orderRepository.searchOrders(keyword, userId, status, pageable);
        
        // 简化处理：每个订单单独查询用户信息（实际应批量查询优化）
        return orderPage.map(order -> {
            UserInfoDTO userInfo = userServiceFeignClient.getUserById(order.getUserId());
            return convertToDTO(order, userInfo);
        });
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long id, Integer status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("订单不存在: " + id));
        
        // 状态流转校验（简化版）
        if (order.getStatus() == 4) {
            throw new IllegalStateException("已取消的订单不能修改状态");
        }
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        UserInfoDTO userInfo = userServiceFeignClient.getUserById(order.getUserId());
        
        log.info("订单状态更新: {}, 新状态: {}", order.getOrderNo(), status);
        return convertToDTO(updatedOrder, userInfo);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("订单不存在: " + id));
        
        // 只有待支付订单可以取消
        if (order.getStatus() != 0) {
            throw new IllegalStateException("只有待支付订单可以取消");
        }
        
        order.setStatus(4); // 已取消
        orderRepository.save(order);
        
        log.info("订单已取消: {}", order.getOrderNo());
    }

    /**
     * 实体转DTO，附带用户信息
     */
    private OrderDTO convertToDTO(Order order, UserInfoDTO userInfo) {
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(order, dto);
        dto.setUserInfo(userInfo);
        return dto;
    }
}