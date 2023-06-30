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
public class ShoppingCartResponse {

    private String id;

    private String courseId;

    private String courseTitle;

    private String courseBanner;

    private String instructorName;

    private Long totalLectures;

    private Long durationInHours;

    private PriceResponse price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
