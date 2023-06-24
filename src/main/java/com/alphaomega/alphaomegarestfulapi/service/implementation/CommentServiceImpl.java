package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Comment;
import com.alphaomega.alphaomegarestfulapi.entity.Course;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotValidException;
import com.alphaomega.alphaomegarestfulapi.payload.request.CommentRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CommentResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.RoleResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.UserResponse;
import com.alphaomega.alphaomegarestfulapi.repository.CommentRepository;
import com.alphaomega.alphaomegarestfulapi.repository.CourseRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserRepository;
import com.alphaomega.alphaomegarestfulapi.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    private CommentRepository commentRepository;

    private CourseRepository courseRepository;

    private UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public CommentResponse create(CommentRequest commentRequest, String userId, String courseId) {
        log.info("Post comment for course id {}", courseId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DataNotFoundException("Course not found"));

        Comment comment = new Comment();
        comment.setId("cm".concat(UUID.randomUUID().toString()));
        comment.setUser(user);
        comment.setCourse(course);
        comment.setReviews(commentRequest.getReviews());
        comment.setTotalStars(commentRequest.getTotalStars());
        comment.setCountLikes(0L);
        comment.setCountDislikes(0L);
        comment.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        comment.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        commentRepository.save(comment);
        log.info("Successfully save comment for course id {}", courseId);

        // update average course rating
        Integer countOfComment = commentRepository.findCountOfComment(course.getId());
        Integer totalOfStars = commentRepository.findTotalOfStars(courseId);


        course.setRating(Double.valueOf(totalOfStars) / Double.valueOf(countOfComment));
        course.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        courseRepository.save(course);
        log.info("Successfully update average rating of course");

        Set<RoleResponse> userRoleResponses = new HashSet<>();
        user.getRoles().stream()
                .forEach(role -> {
                    RoleResponse roleResponse = RoleResponse.builder()
                            .id(role.getId())
                            .role(role.getName().name())
                            .build();
                    userRoleResponses.add(roleResponse);
                });

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setReviews(comment.getReviews());
        commentResponse.setTotalStars(commentRequest.getTotalStars());
        commentResponse.setCountDislikes(commentResponse.getCountDislikes());
        commentResponse.setCountLikes(commentResponse.getCountLikes());
        commentResponse.setUser(UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(userRoleResponses)
                .imageUrl(user.getImageUrl())
                .instructorId(user.getInstructor() != null ? user.getInstructor().getId() : null)
                .instructorName(user.getInstructorName())
                .caption(user.getCaption())
                .biography(user.getBiography())
                .deleted(user.getDeleted())
                .isVerify(user.getIsVerify())
                .webUrl(user.getUserSocialMedia() != null ? user.getUserSocialMedia().getWebUrl() : null)
                .facebookUrl(user.getUserSocialMedia() != null ? user.getUserSocialMedia().getFacebook() : null)
                .linkedinUrl(user.getUserSocialMedia() != null ? user.getUserSocialMedia().getLinkedinUrl() : null)
                .youtubeUrl(user.getUserSocialMedia() != null ? user.getUserSocialMedia().getYoutubeUrl() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build());
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setUpdatedAt(comment.getUpdatedAt());
        return commentResponse;
    }

    @Transactional
    @Override
    public List<CommentResponse> findByCourseId(String courseId) {
        log.info("find comment by id");
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DataNotFoundException("Course not found"));

        List<CommentResponse> commentResponses = new ArrayList<>();
        commentRepository.findByCourseId(course.getId())
                .stream().forEach(comment -> {
                    User user = userRepository.findById(comment.getUser().getId())
                            .orElseThrow(() -> new DataNotFoundException("User not found"));
                    Set<RoleResponse> userRoleResponses = new HashSet<>();
                    user.getRoles().stream()
                            .forEach(role -> {
                                RoleResponse roleResponse = RoleResponse.builder()
                                        .id(role.getId())
                                        .role(role.getName().name())
                                        .build();
                                userRoleResponses.add(roleResponse);
                            });

                    CommentResponse commentResponse = new CommentResponse();
                    commentResponse.setId(comment.getId());
                    commentResponse.setReviews(comment.getReviews());
                    commentResponse.setTotalStars(comment.getTotalStars());
                    commentResponse.setCountDislikes(commentResponse.getCountDislikes());
                    commentResponse.setCountLikes(commentResponse.getCountLikes());
                    commentResponse.setUser(UserResponse.builder()
                            .id(user.getId())
                            .fullName(user.getFullName())
                            .email(user.getEmail())
                            .roles(userRoleResponses)
                            .imageUrl(user.getImageUrl())
                            .instructorId(user.getInstructor() != null ? user.getInstructor().getId() : null)
                            .instructorName(user.getInstructorName())
                            .caption(user.getCaption())
                            .biography(user.getBiography())
                            .deleted(user.getDeleted())
                            .isVerify(user.getIsVerify())
                            .webUrl(user.getUserSocialMedia() != null ? user.getUserSocialMedia().getWebUrl() : null)
                            .facebookUrl(user.getUserSocialMedia() != null ? user.getUserSocialMedia().getFacebook() : null)
                            .linkedinUrl(user.getUserSocialMedia() != null ? user.getUserSocialMedia().getLinkedinUrl() : null)
                            .youtubeUrl(user.getUserSocialMedia() != null ? user.getUserSocialMedia().getYoutubeUrl() : null)
                            .createdAt(user.getCreatedAt())
                            .updatedAt(user.getUpdatedAt())
                            .build());
                    commentResponse.setCreatedAt(comment.getCreatedAt());
                    commentResponse.setUpdatedAt(comment.getUpdatedAt());

                    commentResponses.add(commentResponse);
                });
        log.info("Successfully find all comment by course id");
        return commentResponses;
    }

    @Transactional
    @Override
    public Boolean deleteById(String userId, String commentId) {
        log.info("Delete comment with id {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new DataNotValidException("Oops something when wrong, you don't have access");
        }
        commentRepository.delete(comment);
        log.info("Successfully delete comment");

        Course course = courseRepository.findById(comment.getCourse().getId())
                .orElseThrow(() -> new DataNotFoundException("Course not found"));
        // update average course rating
        Integer countOfComment = commentRepository.findCountOfComment(comment.getCourse().getId());
        Integer totalOfStars = commentRepository.findTotalOfStars(comment.getCourse().getId());

        course.setRating(Double.valueOf(totalOfStars) / Double.valueOf(countOfComment));
        course.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        courseRepository.save(course);
        log.info("Successfully update average rating of course");

        return true;
    }
}
