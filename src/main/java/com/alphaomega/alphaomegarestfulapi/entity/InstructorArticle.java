package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "instructor_article")
public class InstructorArticle {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @URL
    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
