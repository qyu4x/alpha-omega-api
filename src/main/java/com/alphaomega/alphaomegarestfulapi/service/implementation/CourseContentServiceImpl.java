package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import com.alphaomega.alphaomegarestfulapi.entity.CourseContent;
import com.alphaomega.alphaomegarestfulapi.entity.CourseDetail;
import com.alphaomega.alphaomegarestfulapi.exception.BadRequestException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.request.CourseContentRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseContentResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseDetailResponse;
import com.alphaomega.alphaomegarestfulapi.repository.CourseContentRepository;
import com.alphaomega.alphaomegarestfulapi.repository.CourseRepository;
import com.alphaomega.alphaomegarestfulapi.service.CourseContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CourseContentServiceImpl implements CourseContentService {

    private static final Logger log = LoggerFactory.getLogger(CourseContentServiceImpl.class);

    private CourseContentRepository courseContentRepository;

    private CourseRepository courseRepository;

    public CourseContentServiceImpl(CourseContentRepository courseContentRepository, CourseRepository courseRepository) {
        this.courseContentRepository = courseContentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    @Override
    public List<CourseContentResponse> create(List<CourseContentRequest> courseContentRequests, String instructorId, String courseId) {
        log.info("Create course content");
        if (instructorId == null || courseId == null) {
            throw new BadRequestException("Instructor id or Course id can't be emtpy");
        }
        Course course = courseRepository.findByIdAndInstructorId(courseId, instructorId)
                .orElseThrow(() -> new DataNotFoundException("Instructor or Course not found"));

        List<CourseContent> courseContents = new ArrayList<>();
        courseContentRequests.stream().forEach(courseContentRequest -> {
            CourseContent courseContent = new CourseContent();
            courseContent.setId("cxct-".concat(UUID.randomUUID().toString()));
            courseContent.setTitleSubCourse(courseContentRequest.getTitleSubCourse());
            courseContent.setCourse(course);
            courseContent.setDuration(0);
            courseContent.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseContent.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            courseContents.add(courseContent);
        });

        courseContentRepository.saveAll(courseContents);
        log.info("Successfully create new course content for course id {}", courseId);

        List<CourseContentResponse> courseContentResponses = new ArrayList<>();
        courseContents.stream().forEach(courseContent -> {
            CourseContentResponse courseContentResponse = new CourseContentResponse();
            courseContentResponse.setId(courseContent.getId());
            courseContentResponse.setTitleSubCourse(courseContent.getTitleSubCourse());
            courseContentResponse.setTotalDuration("0min");
            courseContentResponse.setTotalLectures(0);
            courseContentResponse.setCreatedAt(courseContent.getCreatedAt());
            courseContentResponse.setUpdatedAt(courseContent.getUpdatedAt());

            courseContentResponses.add(courseContentResponse);
        });

        return courseContentResponses;
    }
}
