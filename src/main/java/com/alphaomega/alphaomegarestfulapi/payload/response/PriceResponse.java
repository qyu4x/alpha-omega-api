package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceResponse {

    private BigDecimal amount;

    private String currencyCode;

    private String display;

}
