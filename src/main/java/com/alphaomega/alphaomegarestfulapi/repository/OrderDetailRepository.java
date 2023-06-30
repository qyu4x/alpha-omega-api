package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    List<OrderDetail> findOrderDetailByCourseId(String courseId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM order_detail WHERE course_id = :id"

    )
    void deleteOrderDetailByCourseId(@Param("id") String id);

}
