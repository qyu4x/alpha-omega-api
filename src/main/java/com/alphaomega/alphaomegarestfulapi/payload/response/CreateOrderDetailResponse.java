package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDetailResponse {

    private List<OrderDetailResponse> course;

    private BigDecimal originalPrice;

    private BigDecimal priceWithDiscount;

    private BigDecimal totalDiscount;

}
