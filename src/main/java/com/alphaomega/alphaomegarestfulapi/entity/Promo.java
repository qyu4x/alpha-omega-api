package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "promo")
public class Promo {

    @Id
    private String id;

    @Size(max = 20)
    private String code;

    private LocalDate expirationDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal totalDiscount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
