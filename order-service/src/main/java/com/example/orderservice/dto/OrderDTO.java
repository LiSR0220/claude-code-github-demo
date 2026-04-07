package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单数据传输对象
 * 
 * @author LiSR0220
 * @date 2026-04-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private String orderNo;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "商品名称不能为空")
    private String productName;

    @NotNull(message = "商品数量不能为空")
    @Positive(message = "数量必须大于0")
    private Integer quantity;

    @NotNull(message = "订单金额不能为空")
    @Positive(message = "金额必须大于0")
    private BigDecimal totalAmount;

    private Integer status;

    private String address;

    private String remark;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    // 用户信息（通过 Feign 获取）
    private UserInfoDTO userInfo;
}