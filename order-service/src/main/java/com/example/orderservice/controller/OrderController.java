package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.service.OrderService;
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
 * 订单控制器
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("创建订单请求: userId={}, product={}", 
                orderDTO.getUserId(), orderDTO.getProductName());
        OrderDTO created = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 根据ID查询订单（含用户信息）
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable @Min(1) Long id) {
        log.info("查询订单请求: id={}", id);
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * 根据订单号查询
     */
    @GetMapping("/orderNo/{orderNo}")
    public ResponseEntity<OrderDTO> getOrderByOrderNo(@PathVariable String orderNo) {
        log.info("查询订单请求: orderNo={}", orderNo);
        OrderDTO order = orderService.getOrderByOrderNo(orderNo);
        return ResponseEntity.ok(order);
    }

    /**
     * 查询用户订单列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderDTO>> listUserOrders(
            @PathVariable @Min(1) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        
        log.info("查询用户订单: userId={}, page={}, size={}", userId, page, size);
        
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<OrderDTO> orderPage = orderService.listUserOrders(userId, pageable);
        return ResponseEntity.ok(orderPage);
    }

    /**
     * 搜索订单
     */
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> searchOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("搜索订单: keyword={}, userId={}, status={}", keyword, userId, status);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderDTO> orderPage = orderService.searchOrders(keyword, userId, status, pageable);
        
        return ResponseEntity.ok(orderPage);
    }

    /**
     * 更新订单状态
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable @Min(1) Long id,
            @RequestParam Integer status) {
        
        log.info("更新订单状态: id={}, status={}", id, status);
        OrderDTO updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable @Min(1) Long id) {
        log.info("取消订单: id={}", id);
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "order-service");
        response.put("status", "UP");
        response.put("feign", "enabled");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}