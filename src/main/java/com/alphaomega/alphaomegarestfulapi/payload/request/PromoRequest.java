package com.alphaomega.alphaomegarestfulapi.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromoRequest {

    @NotBlank(message = "Promo code can't be empty")
    @Size(max = 20, min = 4, message = "Promos must have a minimum of 4 words and a maximum of 20 words")
    private String code;

    @DecimalMin(value = "0.0", message = "Discount must be at least Rp. 0")
    @NotNull(message = "total discount can't be empty")
    private BigDecimal totalDiscount;

    @NotBlank(message = "Description can't be empty")
    private String description;

    @Future(message = "The duration of the promo should be in the future")
    private LocalDate expirationDate;

}
