package com.alphaomega.alphaomegarestfulapi.payload.request;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {


    @Column(columnDefinition = "TEXT")
    private String reviews;

    @Max(value = 5, message = "Stars must be less than or equals to 5")
    @Min(value = 1, message = "Stars must be greater than or equals 1")
    private Integer totalStars;


}
