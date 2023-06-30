package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "banner")
public class Banner {

    @Id
    private String id;

    @URL
    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
