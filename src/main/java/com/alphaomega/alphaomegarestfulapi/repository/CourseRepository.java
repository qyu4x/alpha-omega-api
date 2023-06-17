package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findByIdAndInstructorId(String courseId, String instructorId);

    Page<Course> findAll(Pageable pageable);

    Page<Course> findAllByCourseCategoryId(String courseCategoryId, Pageable pageable);

    Page<Course> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

}
