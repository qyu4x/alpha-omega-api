package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import com.alphaomega.alphaomegarestfulapi.entity.CourseContent;
import com.alphaomega.alphaomegarestfulapi.exception.BadRequestException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.request.CourseContentRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseContentResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseDetailResponse;
import com.alphaomega.alphaomegarestfulapi.repository.CourseContentRepository;
import com.alphaomega.alphaomegarestfulapi.repository.CourseRepository;
import com.alphaomega.alphaomegarestfulapi.service.CourseContentService;
import com.alphaomega.alphaomegarestfulapi.util.DurationUtil;
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
import java.util.concurrent.atomic.AtomicReference;

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

    @Transactional
    @Override
    public CourseContentResponse findById(String courseContentId) {
        log.info("Find course content by id");
        CourseContent courseContent = courseContentRepository.findById(courseContentId)
                .orElseThrow(() -> new DataNotFoundException("Course content not found"));

        CourseContentResponse courseContentResponse = new CourseContentResponse();
        AtomicReference<Integer> totalLectures = new AtomicReference<>(0);
        AtomicReference<Integer> totalDurationInSecond = new AtomicReference<>(0);
        List<CourseDetailResponse> courseDetailResponses = new ArrayList<>();
        if (courseContent.getCourseDetails() != null) {
            courseContent.getCourseDetails().stream().forEach(courseDetail -> {
                totalLectures.getAndSet(totalLectures.get() + 1);
                totalDurationInSecond.getAndSet(totalDurationInSecond.get() + courseDetail.getDuration());
                CourseDetailResponse courseDetailResponse = new CourseDetailResponse();

                courseDetailResponse.setId(courseDetail.getId());
                courseDetailResponse.setTitle(courseDetail.getTitle());
                courseDetailResponse.setVideoUrl(courseDetail.getVideoUrl());
                courseDetailResponse.setIsLocked(true);
                courseDetailResponse.setDuration(DurationUtil.getVideoDurationDisplayFormat(courseDetail.getDuration()));
                courseDetailResponse.setCreatedAt(courseDetail.getCreatedAt());
                courseDetailResponse.setUpdatedAt(courseDetail.getCreatedAt());

                courseDetailResponses.add(courseDetailResponse);
            });
        }
        log.info("Successfully find course content by id");
        courseContentResponse.setId(courseContent.getId());
        courseContentResponse.setTitleSubCourse(courseContent.getTitleSubCourse());
        courseContentResponse.setTotalDuration(DurationUtil.getVideoDurationDisplayFormat(totalDurationInSecond.get()));
        courseContentResponse.setTotalLectures(totalLectures.get());
        courseContentResponse.setCourseDetails(courseDetailResponses);
        courseContentResponse.setCreatedAt(courseContent.getCreatedAt());
        courseContentResponse.setUpdatedAt(courseContent.getUpdatedAt());

        return courseContentResponse;
    }

    @Transactional
    @Override
    public List<CourseContentResponse> findAllByCourseId(String courseId) {
        log.info("Find all course content by course id");
        if (!courseRepository.existsById(courseId)) {
            throw new DataNotFoundException("Course not found");
        }
        List<CourseContentResponse> courseContentResponses = new ArrayList<>();
        courseContentRepository.findAllByCourseId(courseId).stream().forEach(courseContent -> {
            CourseContentResponse courseContentResponse = new CourseContentResponse();
            AtomicReference<Integer> totalLectures = new AtomicReference<>(0);
            AtomicReference<Integer> totalDurationInSecond = new AtomicReference<>(0);
            List<CourseDetailResponse> courseDetailResponses = new ArrayList<>();
            if (courseContent.getCourseDetails() != null) {
                courseContent.getCourseDetails().stream().forEach(courseDetail -> {
                    totalLectures.getAndSet(totalLectures.get() + 1);
                    totalDurationInSecond.getAndSet(totalDurationInSecond.get() + courseDetail.getDuration());
                    CourseDetailResponse courseDetailResponse = new CourseDetailResponse();

                    courseDetailResponse.setId(courseDetail.getId());
                    courseDetailResponse.setTitle(courseDetail.getTitle());
                    courseDetailResponse.setVideoUrl(courseDetail.getVideoUrl());
                    courseDetailResponse.setIsLocked(true);
                    courseDetailResponse.setDuration(DurationUtil.getVideoDurationDisplayFormat(courseDetail.getDuration()));
                    courseDetailResponse.setCreatedAt(courseDetail.getCreatedAt());
                    courseDetailResponse.setUpdatedAt(courseDetail.getCreatedAt());

                    courseDetailResponses.add(courseDetailResponse);
                });
            }
            courseContentResponse.setId(courseContent.getId());
            courseContentResponse.setTitleSubCourse(courseContent.getTitleSubCourse());
            courseContentResponse.setTotalDuration(DurationUtil.getVideoDurationDisplayFormat(totalDurationInSecond.get()));
            courseContentResponse.setTotalLectures(totalLectures.get());
            courseContentResponse.setCourseDetails(courseDetailResponses);
            courseContentResponse.setCreatedAt(courseContent.getCreatedAt());
            courseContentResponse.setUpdatedAt(courseContent.getUpdatedAt());

            courseContentResponses.add(courseContentResponse);
        });
        log.info("Successfully find all content by id");
        return courseContentResponses;
    }

    @Transactional
    @Override
    public Boolean deleteById(String courseContentId) {
        log.info("Delete course content by id");
        courseContentRepository.findById(courseContentId)
                        .orElseThrow(() -> new DataNotFoundException("Course content not found"));
        courseContentRepository.deleteById(courseContentId);
        log.info("Successfully delete course content by id");
        return true;
    }


    @Transactional
    @Override
    public CourseContentResponse update(CourseContentRequest courseContentRequest, String courseContentId) {
        log.info("Update course content with id {}", courseContentRequest);
        CourseContent courseContent = courseContentRepository.findById(courseContentId)
                .orElseThrow(() -> new DataNotFoundException("Course content not found"));

        courseContent.setTitleSubCourse(courseContentRequest.getTitleSubCourse());
        courseContent.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        courseContentRepository.save(courseContent);
        CourseContentResponse courseContentResponse = new CourseContentResponse();
        AtomicReference<Integer> totalLectures = new AtomicReference<>(0);
        AtomicReference<Integer> totalDurationInSecond = new AtomicReference<>(0);
        List<CourseDetailResponse> courseDetailResponses = new ArrayList<>();
        if (courseContent.getCourseDetails() != null) {
            courseContent.getCourseDetails().stream().forEach(courseDetail -> {
                totalLectures.getAndSet(totalLectures.get() + 1);
                totalDurationInSecond.getAndSet(totalDurationInSecond.get() + courseDetail.getDuration());
                CourseDetailResponse courseDetailResponse = new CourseDetailResponse();

                courseDetailResponse.setId(courseDetail.getId());
                courseDetailResponse.setTitle(courseDetail.getTitle());
                courseDetailResponse.setVideoUrl(courseDetail.getVideoUrl());
                courseDetailResponse.setIsLocked(true);
                courseDetailResponse.setDuration(DurationUtil.getVideoDurationDisplayFormat(courseDetail.getDuration()));
                courseDetailResponse.setCreatedAt(courseDetail.getCreatedAt());
                courseDetailResponse.setUpdatedAt(courseDetail.getCreatedAt());

                courseDetailResponses.add(courseDetailResponse);
            });
        }
        log.info("Successfully update course content by id");
        courseContentResponse.setId(courseContent.getId());
        courseContentResponse.setTitleSubCourse(courseContent.getTitleSubCourse());
        courseContentResponse.setTotalDuration(DurationUtil.getVideoDurationDisplayFormat(totalDurationInSecond.get()));
        courseContentResponse.setTotalLectures(totalLectures.get());
        courseContentResponse.setCourseDetails(courseDetailResponses);
        courseContentResponse.setCreatedAt(courseContent.getCreatedAt());
        courseContentResponse.setUpdatedAt(courseContent.getUpdatedAt());

        return courseContentResponse;
    }


}
