package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseStatistics;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findByIdAndInstructorId(String courseId, String instructorId);

    Page<Course> findAll(Pageable pageable);

    Page<Course> findAllByCourseCategoryId(String courseCategoryId, Pageable pageable);

    Page<Course> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT SUM(cd.duration_in_second) as totalDuration, COUNT(cd.title) as lectureCount FROM course as c\n" +
                    "    INNER JOIN course_content cc on c.id = cc.course_id\n" +
                    "INNER JOIN course_detail cd on cc.id = cd.course_content_id WHERE c.id = :courseId"
    )
    List<Object[]> getTotalDurationCourseInSecondAndTotalLecture(@Param("courseId") String courseId);

}
