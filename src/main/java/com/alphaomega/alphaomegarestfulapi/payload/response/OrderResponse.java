package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private String orderId;

    private List<OrderDetailResponse> orderedCourses;

    private BigDecimal originalPrice;

    private BigDecimal priceWithDiscount;

    private BigDecimal totalDiscount;

    private Boolean paymentStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
