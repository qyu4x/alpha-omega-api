package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseContentRepository extends JpaRepository<CourseContent, String> {
    List<CourseContent> findCourseContentByIdAndCourseId(String courseContentId, String courseId);

}
