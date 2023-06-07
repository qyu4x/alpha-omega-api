package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.*;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.request.CourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.*;
import com.alphaomega.alphaomegarestfulapi.repository.CourseCategoryRepository;
import com.alphaomega.alphaomegarestfulapi.repository.CourseRepository;
import com.alphaomega.alphaomegarestfulapi.repository.InstructorRepository;
import com.alphaomega.alphaomegarestfulapi.service.CourseService;
import com.alphaomega.alphaomegarestfulapi.util.CurrencyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public CourseServiceImpl(CourseRepository courseRepository, InstructorRepository instructorRepository, CourseCategoryRepository courseCategoryRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.courseCategoryRepository = courseCategoryRepository;
    }

    @Override
    public CourseResponse create(String instructorId, CourseRequest courseRequest) {
        log.info("Create course for instructor id {}", instructorId);
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new DataNotFoundException("Instructor not found"));

        CourseCategory courseCategory = courseCategoryRepository.findById(courseRequest.getCourseCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Course category not found"));

        List<CourseLesson> courseLessons = new ArrayList<>();
        courseRequest.getLessons().stream().forEach(lessonRequest -> {
            CourseLesson courseLesson = new CourseLesson();
            courseLesson.setId("cxl-".concat(UUID.randomUUID().toString()));
            courseLesson.setName(courseLesson.getName());
            courseLesson.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseLesson.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            courseLessons.add(courseLesson);
        });

        List<CourseRequirement> courseRequirements = new ArrayList<>();
        courseRequest.getRequirements().stream().forEach(requirementRequest -> {
            CourseRequirement courseRequirement = new CourseRequirement();
            courseRequirement.setId("cxr-".concat(UUID.randomUUID().toString()));
            courseRequirement.setName(courseRequirement.getName());
            courseRequirement.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseRequirement.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            courseRequirements.add(courseRequirement);
        });

        Course course = new Course();
        course.setId("cxc-".concat(UUID.randomUUID().toString()));
        course.setInstructor(instructor);
        course.setCourseLessons(courseLessons);
        course.setCourseRequirements(courseRequirements);
        course.setCourseCategory(courseCategory);
        course.setTitle(courseRequest.getTitle());
        course.setDescription(course.getDescription());
        course.setDetailDescription(course.getDetailDescription());
        course.setPrice(courseRequest.getPrice());
        course.setTotalParticipant(0L);
        course.setRating(0.0);
        course.setLanguage(course.getLanguage());
        course.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        course.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

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
        courseResponse.setDetailDescription(course.getDetailDescription());
        courseResponse.setCourseCategory(courseCategoryResponse);
        courseResponse.setPrice(priceResponse);
        courseResponse.setLessons(courseLessonResponses);
        courseResponse.setRequirements(courseRequirementResponses);
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setUpdatedAt(course.getUpdatedAt());

        return courseResponse;
    }
}
