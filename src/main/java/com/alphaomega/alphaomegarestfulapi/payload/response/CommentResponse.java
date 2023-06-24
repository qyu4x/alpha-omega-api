package com.alphaomega.alphaomegarestfulapi.payload.response;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private String id;

    private Integer totalStars;

    private String reviews;

    private Long countLikes;

    private Long countDislikes;

    private UserResponse user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
