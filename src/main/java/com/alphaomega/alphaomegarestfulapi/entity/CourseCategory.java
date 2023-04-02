package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_categories")
public class CourseCategory {

    @Id
    private String id;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Course> course;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
