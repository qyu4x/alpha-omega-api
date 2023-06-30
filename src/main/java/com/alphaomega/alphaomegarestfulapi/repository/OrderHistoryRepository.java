package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, String> {

    List<OrderHistory> findOrderHistoryByUserIdAndCourseId(String userId, String courseId);

    List<OrderHistory> findOrderHistoriesByUserId(String userId);

}
