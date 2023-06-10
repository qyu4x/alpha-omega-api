package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.CourseContent;
import com.alphaomega.alphaomegarestfulapi.entity.CourseDetail;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.response.CloudUploadVideoResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseContentResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseDetailResponse;
import com.alphaomega.alphaomegarestfulapi.repository.CourseContentRepository;
import com.alphaomega.alphaomegarestfulapi.repository.CourseDetailRepository;
import com.alphaomega.alphaomegarestfulapi.repository.CourseRepository;
import com.alphaomega.alphaomegarestfulapi.service.CourseDetailService;
import com.alphaomega.alphaomegarestfulapi.service.FirebaseCloudStorageService;
import com.alphaomega.alphaomegarestfulapi.util.DurationUtil;
import com.alphaomega.alphaomegarestfulapi.util.UploadFileCheckerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class CourseDetailServiceImpl implements CourseDetailService {

    private static final Logger log = LoggerFactory.getLogger(CourseDetailServiceImpl.class);

    private CourseDetailRepository courseDetailRepository;

    private CourseRepository courseRepository;

    private CourseContentRepository courseContentRepository;

    private FirebaseCloudStorageService firebaseCloudStorageService;

    public CourseDetailServiceImpl(CourseDetailRepository courseDetailRepository, CourseRepository courseRepository, CourseContentRepository courseContentRepository, FirebaseCloudStorageService firebaseCloudStorageService) {
        this.courseDetailRepository = courseDetailRepository;
        this.courseRepository = courseRepository;
        this.courseContentRepository = courseContentRepository;
        this.firebaseCloudStorageService = firebaseCloudStorageService;
    }

    @Transactional
    @Override
    public CourseDetailResponse create(String title, MultipartFile videoSource, String courseId, String courseContentId) throws IOException {
        log.info("Do upload video for  course id {}", courseId);
        if (courseContentRepository.findCourseContentByIdAndCourseId(courseContentId, courseId).isEmpty()) {
            throw new DataNotFoundException("Oops course or course content not found");
        }
        CourseContent courseContent = courseContentRepository.findById(courseContentId)
                .orElseThrow(() -> new DataNotFoundException("Course not found"));

        UploadFileCheckerUtil.uploadVideoChecker(title, videoSource);
        CloudUploadVideoResponse cloudUploadVideoResponse = firebaseCloudStorageService.doUploadVideoFile(videoSource);
        log.info("duration video is {} second", cloudUploadVideoResponse.getDurationInSecond());

        CourseDetail courseDetail = new CourseDetail();
        courseDetail.setId("cdt-".concat(UUID.randomUUID().toString()));
        courseDetail.setTitle(title);
        courseDetail.setDuration(cloudUploadVideoResponse.getDurationInSecond());
        courseDetail.setVideoUrl(cloudUploadVideoResponse.getVideoUrl());
        courseDetail.setCourseContent(courseContent);
        courseDetail.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        courseDetail.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        courseDetailRepository.save(courseDetail);
        log.info("Successfully upload video with title {}", title);

        CourseDetailResponse courseDetailResponse = new CourseDetailResponse();
        courseDetailResponse.setId(courseDetail.getId());
        courseDetailResponse.setTitle(courseDetail.getTitle());
        courseDetailResponse.setVideoUrl(courseDetail.getVideoUrl());
        courseDetailResponse.setDuration(DurationUtil.getVideoDurationDisplayFormat(courseDetail.getDuration()));
        courseDetailResponse.setCreatedAt(courseDetail.getCreatedAt());
        courseDetailResponse.setUpdatedAt(courseDetail.getCreatedAt());

        return courseDetailResponse;
    }
}
