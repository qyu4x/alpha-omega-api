package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.OrderHistory;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseResponse;
import com.alphaomega.alphaomegarestfulapi.repository.OrderHistoryRepository;
import com.alphaomega.alphaomegarestfulapi.service.CourseService;
import com.alphaomega.alphaomegarestfulapi.service.MyCourseService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MyCourseServiceImpl implements MyCourseService {

    private final static Logger log = LoggerFactory.getLogger(MyCourseServiceImpl.class);


    private CourseService courseService;

    private OrderHistoryRepository orderHistoryRepository;

    public MyCourseServiceImpl(CourseService courseService, OrderHistoryRepository orderHistoryRepository) {
        this.courseService = courseService;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Transactional
    @Override
    public List<CourseResponse> findAll(String userId) {
        log.info("Find my course by course id");
        List<OrderHistory> orderHistories = orderHistoryRepository.findOrderHistoriesByUserId(userId);

        List<CourseResponse> courseResponses = new ArrayList<>();
        orderHistories.stream().forEach(orderHistory -> {
            CourseResponse courseResponse = courseService.findById(orderHistory.getCourse().getId());
            courseResponses.add(courseResponse);

        });
        log.info("Successfully find all my course");
        return courseResponses;
    }
}
