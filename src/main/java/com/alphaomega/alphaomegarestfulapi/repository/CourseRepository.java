package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findByIdAndInstructorId(String courseId, String instructorId);

}
