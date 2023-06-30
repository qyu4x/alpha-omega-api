package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "instructor")
public class Instructor {

    @Id
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;

    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InstructorArticle> instructorArticles;

    private Long totalParticipants;

    private Long totalReviews;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
