package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseContentRepository extends JpaRepository<CourseContent, String> {
    List<CourseContent> findCourseContentByIdAndCourseId(String courseContentId, String courseId);

    @Query(
            nativeQuery = true,
            value = "SELECT SUM(cd.duration_in_second) FROM course_content AS cc \n" +
                    "    INNER JOIN course_detail cd on cc.id = cd.course_content_id WHERE cc.id = :courseContentId\n"
    )
    Integer findTotalDurationOfContentDetailByContentId(@Param("courseContentId") String courseContentId);

    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(cd.duration_in_second) FROM course_content AS cc \n" +
                    "    INNER JOIN course_detail cd on cc.id = cd.course_content_id WHERE cc.id = :courseContentId\n"
    )
    Integer findCountOfContentDetailByContentId(@Param("courseContentId") String courseContentId);

    List<CourseContent> findAllByCourseId(String courseId);

}
