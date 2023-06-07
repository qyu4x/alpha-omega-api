package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.*;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.exception.FailedUploadFileException;
import com.alphaomega.alphaomegarestfulapi.payload.request.CourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.UpdateCourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.*;
import com.alphaomega.alphaomegarestfulapi.repository.*;
import com.alphaomega.alphaomegarestfulapi.service.CourseService;
import com.alphaomega.alphaomegarestfulapi.service.FirebaseCloudStorageService;
import com.alphaomega.alphaomegarestfulapi.util.CurrencyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private CourseRepository courseRepository;

    private InstructorRepository instructorRepository;

    private CourseCategoryRepository courseCategoryRepository;

    private CommentRepository commentRepository;

    private OrderDetailRepository orderDetailRepository;

    private FirebaseCloudStorageService firebaseCloudStorageService;

    public CourseServiceImpl(CourseRepository courseRepository, InstructorRepository instructorRepository, CourseCategoryRepository courseCategoryRepository, CommentRepository commentRepository, OrderDetailRepository orderDetailRepository, FirebaseCloudStorageService firebaseCloudStorageService) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.commentRepository = commentRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.firebaseCloudStorageService = firebaseCloudStorageService;
    }

    @Value("${course.banner.default}")
    private String defaultBannerUrl;

    @Override
    public CourseResponse create(String instructorId, CourseRequest courseRequest) {
        log.info("Create course for instructor id {}", instructorId);
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new DataNotFoundException("Instructor not found"));

        CourseCategory courseCategory = courseCategoryRepository.findById(courseRequest.getCourseCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Course category not found"));

        Course course = new Course();
        course.setId("cxc-".concat(UUID.randomUUID().toString()));
        course.setInstructor(instructor);
        course.setCourseCategory(courseCategory);
        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setDetailDescription(courseRequest.getDetailDescription());
        course.setPrice(courseRequest.getPrice());
        course.setBannerUrl(defaultBannerUrl);
        course.setTotalParticipant(0L);
        course.setRating(0.0);
        course.setLanguage(courseRequest.getLanguage());
        course.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        course.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        List<CourseLesson> courseLessons = new ArrayList<>();
        courseRequest.getLessons().stream().forEach(lessonRequest -> {
            CourseLesson courseLesson = new CourseLesson();
            courseLesson.setId("cxl-".concat(UUID.randomUUID().toString()));
            courseLesson.setName(lessonRequest.getName());
            courseLesson.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseLesson.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseLesson.setCourse(course);

            courseLessons.add(courseLesson);
        });

        List<CourseRequirement> courseRequirements = new ArrayList<>();
        courseRequest.getRequirements().stream().forEach(requirementRequest -> {
            CourseRequirement courseRequirement = new CourseRequirement();
            courseRequirement.setId("cxr-".concat(UUID.randomUUID().toString()));
            courseRequirement.setName(requirementRequest.getName());
            courseRequirement.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseRequirement.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseRequirement.setCourse(course);

            courseRequirements.add(courseRequirement);
        });

        course.setCourseRequirements(courseRequirements);
        course.setCourseLessons(courseLessons);

        courseRepository.save(course);
        log.info("Successfully save new course into database");

        CourseCategoryResponse courseCategoryResponse = new CourseCategoryResponse();
        courseCategoryResponse.setId(courseCategory.getId());
        courseCategoryResponse.setName(courseCategory.getName());
        courseCategoryResponse.setCreatedAt(courseCategory.getCreatedAt());
        courseCategoryResponse.setUpdatedAt(courseCategory.getUpdatedAt());

        List<LessonResponse> courseLessonResponses = new ArrayList<>();
        courseLessons.stream().forEach(courseLesson -> {
            LessonResponse courseLessonResponse = new LessonResponse();
            courseLessonResponse.setId(courseLesson.getId());
            courseLessonResponse.setName(courseLesson.getName());
            courseLessonResponse.setCreatedAt(courseLesson.getCreatedAt());
            courseLessonResponse.setUpdatedAt(courseLesson.getUpdatedAt());

            courseLessonResponses.add(courseLessonResponse);
        });

        List<RequirementResponse> courseRequirementResponses = new ArrayList<>();
        courseRequirements.stream().forEach(courseRequirement -> {
            RequirementResponse requirementResponse = new RequirementResponse();
            requirementResponse.setId(courseRequirement.getId());
            requirementResponse.setName(courseRequirement.getName());
            requirementResponse.setCreatedAt(courseRequirement.getCreatedAt());
            requirementResponse.setUpdatedAt(courseRequirement.getUpdatedAt());

            courseRequirementResponses.add(requirementResponse);
        });

        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setAmount(course.getPrice());
        priceResponse.setDisplay(CurrencyUtil.convertToDisplayCurrency(course.getPrice()));
        priceResponse.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());

        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setInstructorId(course.getInstructor().getId());
        courseResponse.setTitle(course.getTitle());
        courseResponse.setRating(course.getRating());
        courseResponse.setTotalParticipant(course.getTotalParticipant());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setLanguage(course.getLanguage());
        courseResponse.setDetailDescription(course.getDetailDescription());
        courseResponse.setBannerUrl(defaultBannerUrl);
        courseResponse.setCourseCategory(courseCategoryResponse);
        courseResponse.setPrice(priceResponse);
        courseResponse.setLessons(courseLessonResponses);
        courseResponse.setRequirements(courseRequirementResponses);
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setUpdatedAt(course.getUpdatedAt());

        return courseResponse;
    }

    @Transactional
    @Override
    public CourseResponse updateBanner(MultipartFile multipartFile, String courseId) {
        log.info("Update banner for course id {}", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DataNotFoundException("Course not found"));

        if (multipartFile.isEmpty()) {
            throw new FailedUploadFileException("Image can't be empty");
        }
        course.setBannerUrl(firebaseCloudStorageService.doUploadImageFile(multipartFile));
        courseRepository.save(course);
        log.info("Successfully update banner for course id {}", courseId);


        CourseCategoryResponse courseCategoryResponse = new CourseCategoryResponse();
        courseCategoryResponse.setId(course.getCourseCategory().getId());
        courseCategoryResponse.setName(course.getCourseCategory().getName());
        courseCategoryResponse.setCreatedAt(course.getCourseCategory().getCreatedAt());
        courseCategoryResponse.setUpdatedAt(course.getCourseCategory().getUpdatedAt());

        List<LessonResponse> courseLessonResponses = new ArrayList<>();
        course.getCourseLessons().stream().forEach(courseLesson -> {
            LessonResponse courseLessonResponse = new LessonResponse();
            courseLessonResponse.setId(courseLesson.getId());
            courseLessonResponse.setName(courseLesson.getName());
            courseLessonResponse.setCreatedAt(courseLesson.getCreatedAt());
            courseLessonResponse.setUpdatedAt(courseLesson.getUpdatedAt());

            courseLessonResponses.add(courseLessonResponse);
        });

        List<RequirementResponse> courseRequirementResponses = new ArrayList<>();
        course.getCourseRequirements().stream().forEach(courseRequirement -> {
            RequirementResponse requirementResponse = new RequirementResponse();
            requirementResponse.setId(courseRequirement.getId());
            requirementResponse.setName(courseRequirement.getName());
            requirementResponse.setCreatedAt(courseRequirement.getCreatedAt());
            requirementResponse.setUpdatedAt(courseRequirement.getUpdatedAt());

            courseRequirementResponses.add(requirementResponse);
        });

        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setAmount(course.getPrice());
        priceResponse.setDisplay(CurrencyUtil.convertToDisplayCurrency(course.getPrice()));
        priceResponse.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());


        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setInstructorId(course.getInstructor().getId());
        courseResponse.setTitle(course.getTitle());
        courseResponse.setRating(course.getRating());
        courseResponse.setTotalParticipant(course.getTotalParticipant());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setLanguage(course.getLanguage());
        courseResponse.setDetailDescription(course.getDetailDescription());
        courseResponse.setBannerUrl(course.getBannerUrl());
        courseResponse.setCourseCategory(courseCategoryResponse);
        courseResponse.setPrice(priceResponse);
        courseResponse.setLessons(courseLessonResponses);
        courseResponse.setRequirements(courseRequirementResponses);
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setUpdatedAt(course.getUpdatedAt());

        return courseResponse;
    }

    @Transactional
    @Override
    public CourseResponse update(UpdateCourseRequest updateCourseRequest, String courseId) {
        log.info("Updat course id {}", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DataNotFoundException("Course not found"));

        course.setTitle(updateCourseRequest.getTitle());
        course.setDescription(updateCourseRequest.getDescription());
        course.setDetailDescription(updateCourseRequest.getDetailDescription());
        courseRepository.save(course);
        log.info("Successfully update course with id {}", courseId);

        CourseCategoryResponse courseCategoryResponse = new CourseCategoryResponse();
        courseCategoryResponse.setId(course.getCourseCategory().getId());
        courseCategoryResponse.setName(course.getCourseCategory().getName());
        courseCategoryResponse.setCreatedAt(course.getCourseCategory().getCreatedAt());
        courseCategoryResponse.setUpdatedAt(course.getCourseCategory().getUpdatedAt());

        List<LessonResponse> courseLessonResponses = new ArrayList<>();
        course.getCourseLessons().stream().forEach(courseLesson -> {
            LessonResponse courseLessonResponse = new LessonResponse();
            courseLessonResponse.setId(courseLesson.getId());
            courseLessonResponse.setName(courseLesson.getName());
            courseLessonResponse.setCreatedAt(courseLesson.getCreatedAt());
            courseLessonResponse.setUpdatedAt(courseLesson.getUpdatedAt());

            courseLessonResponses.add(courseLessonResponse);
        });

        List<RequirementResponse> courseRequirementResponses = new ArrayList<>();
        course.getCourseRequirements().stream().forEach(courseRequirement -> {
            RequirementResponse requirementResponse = new RequirementResponse();
            requirementResponse.setId(courseRequirement.getId());
            requirementResponse.setName(courseRequirement.getName());
            requirementResponse.setCreatedAt(courseRequirement.getCreatedAt());
            requirementResponse.setUpdatedAt(courseRequirement.getUpdatedAt());

            courseRequirementResponses.add(requirementResponse);
        });

        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setAmount(course.getPrice());
        priceResponse.setDisplay(CurrencyUtil.convertToDisplayCurrency(course.getPrice()));
        priceResponse.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());


        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setInstructorId(course.getInstructor().getId());
        courseResponse.setTitle(course.getTitle());
        courseResponse.setRating(course.getRating());
        courseResponse.setTotalParticipant(course.getTotalParticipant());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setLanguage(course.getLanguage());
        courseResponse.setDetailDescription(course.getDetailDescription());
        courseResponse.setBannerUrl(course.getBannerUrl());
        courseResponse.setCourseCategory(courseCategoryResponse);
        courseResponse.setPrice(priceResponse);
        courseResponse.setLessons(courseLessonResponses);
        courseResponse.setRequirements(courseRequirementResponses);
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setUpdatedAt(course.getUpdatedAt());

        return courseResponse;
    }

    @Transactional
    @Override
    public Boolean deleteById(String courseId) {
        log.info("Delete course with id {}", courseId);
        if (commentRepository.findByCourseId(courseId).size() > 0) {
            commentRepository.deleteCommentByCourseId(courseId);
        }

        if (orderDetailRepository.findOrderDetailByCourseId(courseId).size() > 0) {
            orderDetailRepository.deleteOrderDetailByCourseId(courseId);
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DataNotFoundException("Course not found"));
        courseRepository.delete(course);
        log.info("Successfully delete course with id {}", courseId);
        return true;
    }
}
