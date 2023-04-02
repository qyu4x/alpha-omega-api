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

import java.math.BigInteger;
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

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigInteger totalDiscount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
