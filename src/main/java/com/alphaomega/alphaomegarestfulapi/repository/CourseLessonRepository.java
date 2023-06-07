package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.CourseLesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseLessonRepository extends JpaRepository<CourseLesson, String> {
}
