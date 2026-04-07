package com.example.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "订单号不能为空")
    @Column(name = "order_no", unique = true, nullable = false, length = 64)
    private String orderNo;

    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "商品名称不能为空")
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @NotNull(message = "商品数量不能为空")
    @Positive(message = "数量必须大于0")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "订单金额不能为空")
    @Positive(message = "金额必须大于0")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status", nullable = false)
    private Integer status = 0; // 0: 待支付, 1: 已支付, 2: 已发货, 3: 已完成, 4: 已取消

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // 生成订单号: ORD + 年月日 + 6位随机数
        if (orderNo == null) {
            orderNo = generateOrderNo();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateOrderNo() {
        String date = java.time.format.DateTimeFormatter
                .ofPattern("yyyyMMdd")
                .format(LocalDateTime.now());
        String random = String.format("%06d", (int) (Math.random() * 1000000));
        return "ORD" + date + random;
    }
}