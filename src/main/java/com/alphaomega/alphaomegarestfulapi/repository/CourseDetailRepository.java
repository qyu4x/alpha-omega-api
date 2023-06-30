package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.CourseContent;
import com.alphaomega.alphaomegarestfulapi.entity.CourseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDetailRepository extends JpaRepository<CourseDetail, String> {
    List<CourseDetail> findCourseDetailByCourseContentIdOrderByCreatedAtAsc(String id);
}
