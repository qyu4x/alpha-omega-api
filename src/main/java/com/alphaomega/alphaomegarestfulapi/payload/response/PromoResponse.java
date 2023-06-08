package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromoResponse {

    private String id;

    private String code;

    private PriceResponse totalDiscount;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
