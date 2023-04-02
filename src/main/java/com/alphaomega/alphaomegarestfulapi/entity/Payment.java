package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private PaymentMethod paymentMethod;

    private LocalDateTime expiredPayment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
