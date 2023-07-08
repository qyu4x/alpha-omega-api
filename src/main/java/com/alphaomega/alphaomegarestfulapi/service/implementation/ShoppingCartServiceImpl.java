package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import com.alphaomega.alphaomegarestfulapi.entity.ShoppingCart;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.exception.DataAlreadyExistsException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseStatistics;
import com.alphaomega.alphaomegarestfulapi.payload.response.PriceResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.ShoppingCartResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WishListResponse;
import com.alphaomega.alphaomegarestfulapi.repository.CourseRepository;
import com.alphaomega.alphaomegarestfulapi.repository.ShoppingCartRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserRepository;
import com.alphaomega.alphaomegarestfulapi.service.ShoppingCartService;
import com.alphaomega.alphaomegarestfulapi.util.CurrencyUtil;
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
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final static Logger log = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private UserRepository userRepository;

    private CourseRepository courseRepository;

    private ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(UserRepository userRepository, CourseRepository courseRepository, ShoppingCartRepository shoppingCartRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    @Transactional
    public ShoppingCartResponse save(String userId, String courseId) {
        log.info("Save course into shopping cart for user id {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DataNotFoundException("Course not found"));

        if (!shoppingCartRepository.findByCourseIdAAndUserId(courseId, userId).isEmpty()) {
            throw new DataAlreadyExistsException("Oops, this course is already on your shooping cart");
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId("spc-".concat(UUID.randomUUID().toString()));
        shoppingCart.setUser(user);
        shoppingCart.setCourse(course);
        shoppingCart.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        shoppingCart.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        shoppingCartRepository.save(shoppingCart);
        log.info("Successfully insert course into shopping cart");
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

        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse();
        shoppingCartResponse.setId(shoppingCart.getId());
        shoppingCartResponse.setPrice(priceResponse);
        shoppingCartResponse.setCourseBanner(course.getBannerUrl());
        shoppingCartResponse.setCourseId(course.getId());
        shoppingCartResponse.setTotalLectures(courseStatistics.getLectureCount());
        shoppingCartResponse.setDurationInHours(Duration.ofSeconds(courseStatistics.getTotalDuration()).toHours());
        shoppingCartResponse.setInstructorName(course.getInstructor().getUser().getInstructorName() == null ? course.getInstructor().getUser().getFullName() : course.getInstructor().getUser().getInstructorName());
        shoppingCartResponse.setCourseTitle(course.getTitle());
        shoppingCartResponse.setCreatedAt(course.getCreatedAt());
        shoppingCartResponse.setUpdatedAt(course.getUpdatedAt());

        return shoppingCartResponse;
    }

    @Override
    @Transactional
    public List<ShoppingCartResponse> findAllByUserId(String userId) {
        log.info("Find all shopping cart with user id {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<ShoppingCartResponse> shoppingCartResponses = new ArrayList<>();
        shoppingCartRepository.findAllByUserId(userId).stream().forEach(shoppingCart -> {

            List<Object[]> totalDurationCourseInSecondAndTotalLecture = courseRepository
                    .getTotalDurationCourseInSecondAndTotalLecture(shoppingCart.getCourse().getId());
            CourseStatistics courseStatistics = new CourseStatistics();
            for (var wishlist : totalDurationCourseInSecondAndTotalLecture) {
                courseStatistics.setTotalDuration((Long) wishlist[0]);
                courseStatistics.setLectureCount((Long) wishlist[1]);
            }

            PriceResponse priceResponse = new PriceResponse();
            priceResponse.setDisplay(CurrencyUtil.convertToDisplayCurrency(shoppingCart.getCourse().getPrice()));
            priceResponse.setAmount(shoppingCart.getCourse().getPrice());
            priceResponse.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());

            ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse();
            shoppingCartResponse.setId(shoppingCart.getId());
            shoppingCartResponse.setPrice(priceResponse);
            shoppingCartResponse.setCourseBanner(shoppingCart.getCourse().getBannerUrl());
            shoppingCartResponse.setCourseId(shoppingCart.getCourse().getId());
            shoppingCartResponse.setTotalLectures(courseStatistics.getLectureCount());
            shoppingCartResponse.setDurationInHours(Duration.ofSeconds(courseStatistics.getTotalDuration()).toHours());
            shoppingCartResponse.setInstructorName(shoppingCart.getCourse().getInstructor().getUser().getInstructorName() == null ? shoppingCart.getCourse().getInstructor().getUser().getFullName() : shoppingCart.getCourse().getInstructor().getUser().getInstructorName());
            shoppingCartResponse.setCourseTitle(shoppingCart.getCourse().getTitle());
            shoppingCartResponse.setCreatedAt(shoppingCart.getCourse().getCreatedAt());
            shoppingCartResponse.setUpdatedAt(shoppingCart.getCourse().getUpdatedAt());

            shoppingCartResponses.add(shoppingCartResponse);
        });
        log.info("Successfully get all shopping cart");
        return shoppingCartResponses;
    }

    @Override
    @Transactional
    public Boolean deleteById(String shoppingCartId) {
        log.info("Delete shopping cart with id {}", shoppingCartId);
        shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new DataNotFoundException("Shopping cart not found"));
        shoppingCartRepository.deleteById(shoppingCartId);
        log.info("Successfully delete shopping cart");
        return true;
    }
}
