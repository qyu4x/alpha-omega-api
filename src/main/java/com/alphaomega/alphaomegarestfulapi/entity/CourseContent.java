package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_content")
public class CourseContent {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String titleSubCourse;

    @Column(name = "duration_in_second")
    private Integer duration;

    @OneToMany(mappedBy = "courseContent", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CourseDetail> courseDetails;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
