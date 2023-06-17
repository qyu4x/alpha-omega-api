package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_detail")
public class CourseDetail {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "course_content_id")
    private CourseContent courseContent;

    private String title;

    @URL
    private String videoUrl;

    @Column(name = "duration_in_second")
    private Integer duration;

    @OneToMany(mappedBy = "courseDetail", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<UserWatchingHistory> userWatchingHistories;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
