package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerResponse {

    private String id;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
