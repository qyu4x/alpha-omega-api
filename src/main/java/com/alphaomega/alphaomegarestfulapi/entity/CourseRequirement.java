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
@Table(name = "course_requirement")
public class CourseRequirement {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
