package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findByCourseId(String courseId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM comment WHERE course_id = :id"

    )
    void deleteCommentByCourseId(@Param("id") String id);

    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) FROM comment WHERE course_id = :courseId"
    )
    Integer findCountOfComment(@Param("courseId") String courseId);

    @Query(
            nativeQuery = true,
            value = "SELECT SUM(comment.total_stars) FROM comment WHERE course_id = :courseId"
    )
    Integer findTotalOfStars(@Param("courseId") String courseId);

    List<Comment> findAllByCourseId(String courseId);
}
