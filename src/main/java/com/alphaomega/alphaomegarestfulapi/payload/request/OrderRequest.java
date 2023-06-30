package com.alphaomega.alphaomegarestfulapi.payload.request;

import com.alphaomega.alphaomegarestfulapi.entity.OrderDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class OrderRequest {

    @Valid
    private List<OrderDetailRequest> courses;

    private String promoId;

    @DecimalMin(value = "0.0")
    @NotNull(message = "Total amount can't be empty")
    private BigDecimal total;
}
