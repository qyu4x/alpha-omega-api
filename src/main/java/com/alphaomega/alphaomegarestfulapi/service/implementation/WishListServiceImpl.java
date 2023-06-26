package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.entity.WishList;
import com.alphaomega.alphaomegarestfulapi.exception.DataAlreadyExistsException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotValidException;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseStatistics;
import com.alphaomega.alphaomegarestfulapi.payload.response.PriceResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WishListResponse;
import com.alphaomega.alphaomegarestfulapi.repository.CourseRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserRepository;
import com.alphaomega.alphaomegarestfulapi.repository.WishListRepository;
import com.alphaomega.alphaomegarestfulapi.service.WishListService;
import com.alphaomega.alphaomegarestfulapi.util.CurrencyUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WishListServiceImpl implements WishListService {

    private static  final Logger log = LoggerFactory.getLogger(WishListService.class);

    private WishListRepository wishListRepository;

    private CourseRepository courseRepository;

    private UserRepository userRepository;

    public WishListServiceImpl(WishListRepository wishListRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.wishListRepository = wishListRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public WishListResponse save(String userId, String courseId) {
        log.info("Save course into wishlist");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DataNotFoundException("Course not found"));

        if (!wishListRepository.findByCourseId(courseId).isEmpty()) {
            throw new DataAlreadyExistsException("Oops, this course is already on your wish list");
        }

        WishList wishList = new WishList();
        wishList.setId("wl-".concat(UUID.randomUUID().toString()));
        wishList.setCourse(course);
        wishList.setUser(user);
        wishList.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        wishList.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        wishListRepository.save(wishList);

        List<Object[]> totalDurationCourseInSecondAndTotalLecture = courseRepository
                .getTotalDurationCourseInSecondAndTotalLecture(courseId);

        CourseStatistics courseStatistics = new CourseStatistics();
        for (var wishlist : totalDurationCourseInSecondAndTotalLecture) {
            courseStatistics.setTotalDuration((Long) wishlist[0]);
            courseStatistics.setLectureCount((Long) wishlist[1]);
        }

        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setDisplay(CurrencyUtil.convertToDisplayCurrency(course.getPrice()));
        priceResponse.setAmount(course.getPrice());
        priceResponse.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());

        WishListResponse wishListResponse = new WishListResponse();
        wishListResponse.setId(wishList.getId());
        wishListResponse.setPrice(priceResponse);
        wishListResponse.setCourseBanner(course.getBannerUrl());
        wishListResponse.setCourseId(course.getId());
        wishListResponse.setTotalLectures(courseStatistics.getLectureCount());
        wishListResponse.setDurationInHours(Duration.ofSeconds(courseStatistics.getTotalDuration()).toHours());
        wishListResponse.setInstructorName(course.getInstructor().getUser().getInstructorName() == null ? course.getInstructor().getUser().getFullName() : course.getInstructor().getUser().getInstructorName());
        wishListResponse.setCourseTitle(course.getTitle());
        wishListResponse.setCreatedAt(course.getCreatedAt());
        wishListResponse.setUpdatedAt(course.getUpdatedAt());
        log.info("Successfully create wish list");

        return wishListResponse;
    }

    @Transactional
    @Override
    public List<WishListResponse> findAllById(String userId) {
        log.info("Find all wishlist with user id {}", userId);
        userRepository.findById(userId)
                        .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<WishListResponse> wishListResponses = new ArrayList<>();
        wishListRepository.findAllByUserId(userId).stream().forEach(wishList -> {

            List<Object[]> totalDurationCourseInSecondAndTotalLecture = courseRepository
                    .getTotalDurationCourseInSecondAndTotalLecture(wishList.getCourse().getId());
            CourseStatistics courseStatistics = new CourseStatistics();
            for (var wishlist : totalDurationCourseInSecondAndTotalLecture) {
                courseStatistics.setTotalDuration((Long) wishlist[0]);
                courseStatistics.setLectureCount((Long) wishlist[1]);
            }

            PriceResponse priceResponse = new PriceResponse();
            priceResponse.setDisplay(CurrencyUtil.convertToDisplayCurrency(wishList.getCourse().getPrice()));
            priceResponse.setAmount(wishList.getCourse().getPrice());
            priceResponse.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());

            WishListResponse wishListResponse = new WishListResponse();
            wishListResponse.setId(wishList.getId());
            wishListResponse.setPrice(priceResponse);
            wishListResponse.setCourseBanner(wishList.getCourse().getBannerUrl());
            wishListResponse.setCourseId(wishList.getCourse().getId());
            wishListResponse.setTotalLectures(courseStatistics.getLectureCount());
            wishListResponse.setDurationInHours(Duration.ofSeconds(courseStatistics.getTotalDuration()).toHours());
            wishListResponse.setInstructorName(wishList.getCourse().getInstructor().getUser().getInstructorName() == null ? wishList.getCourse().getInstructor().getUser().getFullName() : wishList.getCourse().getInstructor().getUser().getInstructorName());
            wishListResponse.setCourseTitle(wishList.getCourse().getTitle());
            wishListResponse.setCreatedAt(wishList.getCourse().getCreatedAt());
            wishListResponse.setUpdatedAt(wishList.getCourse().getUpdatedAt());

            wishListResponses.add(wishListResponse);
        });
        log.info("Successfully get all wishlists");
        return wishListResponses;
    }

    @Transactional
    @Override
    public Boolean deleteById(String id) {
        log.info("Delete wish list with id {}", id);
        wishListRepository.findById(id)
                        .orElseThrow(() -> new DataNotFoundException("Wish List not found"));
        wishListRepository.deleteById(id);
        log.info("Successfully delete wish list");
        return true;
    }
}
