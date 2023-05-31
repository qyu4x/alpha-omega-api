package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Instructor;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.request.InstructorBiodataRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.InstructorDetailResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.InstructorResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.RoleResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.UserResponse;
import com.alphaomega.alphaomegarestfulapi.repository.InstructorRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserRepository;
import com.alphaomega.alphaomegarestfulapi.service.InstructorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final static Logger log = LoggerFactory.getLogger(InstructorServiceImpl.class);

    private InstructorRepository instructorRepository;

    private UserRepository userRepository;

    public InstructorServiceImpl(InstructorRepository instructorRepository, UserRepository userRepository) {
        this.instructorRepository = instructorRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public InstructorDetailResponse findById(String instructorId) {
        log.info("Find instructor by id");
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new DataNotFoundException("Instructor not found"));
        log.info("Successfully find instructor by id");
        Set<RoleResponse> roleResponses = new HashSet<>();
        instructor.getUser().getRoles().stream()
                .forEach(role -> {
                    RoleResponse roleResponse = RoleResponse.builder()
                            .id(role.getId())
                            .role(role.getName().name())
                            .build();
                    roleResponses.add(roleResponse);
                });

        InstructorResponse instructorResponse = InstructorResponse.builder()
                .id(instructor.getId())
                .userId(instructor.getUser().getId())
                .totalParticipant(instructor.getTotalParticipants())
                .totalReview(instructor.getTotalReviews())
                .createdAt(instructor.getCreatedAt())
                .updatedAt(instructor.getUpdatedAt())
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(instructor.getUser().getId())
                .fullName(instructor.getUser().getFullName())
                .email(instructor.getUser().getEmail())
                .roles(roleResponses)
                .imageUrl(instructor.getUser().getImageUrl())
                .instructorName(instructor.getUser().getInstructorName())
                .caption(instructor.getUser().getCaption())
                .biography(instructor.getUser().getBiography())
                .deleted(instructor.getUser().getDeleted())
                .isVerify(instructor.getUser().getIsVerify())
                .createdAt(instructor.getUser().getCreatedAt())
                .updatedAt(instructor.getUser().getUpdatedAt())
                .build();

        return InstructorDetailResponse.builder()
                .instructor(instructorResponse)
                .userResponse(userResponse)
                .build();
    }

    @Transactional
    @Override
    public List<InstructorDetailResponse> findAll() {
        log.info("Find all instructor");
        List<Instructor> instructors = instructorRepository.findAll();
        log.info("Successfully find all instructors");

        List<InstructorDetailResponse> instructorDetailResponses = new ArrayList<>();
        instructors.stream().forEach(instructor -> {
            Set<RoleResponse> roleResponses = new HashSet<>();
            instructor.getUser().getRoles().stream()
                    .forEach(role -> {
                        RoleResponse roleResponse = RoleResponse.builder()
                                .id(role.getId())
                                .role(role.getName().name())
                                .build();
                        roleResponses.add(roleResponse);
                    });

            InstructorResponse instructorResponse = InstructorResponse.builder()
                    .id(instructor.getId())
                    .userId(instructor.getUser().getId())
                    .totalParticipant(instructor.getTotalParticipants())
                    .totalReview(instructor.getTotalReviews())
                    .createdAt(instructor.getCreatedAt())
                    .updatedAt(instructor.getUpdatedAt())
                    .build();

            UserResponse userResponse = UserResponse.builder()
                    .id(instructor.getUser().getId())
                    .fullName(instructor.getUser().getFullName())
                    .email(instructor.getUser().getEmail())
                    .roles(roleResponses)
                    .imageUrl(instructor.getUser().getImageUrl())
                    .instructorName(instructor.getUser().getInstructorName())
                    .caption(instructor.getUser().getCaption())
                    .biography(instructor.getUser().getBiography())
                    .deleted(instructor.getUser().getDeleted())
                    .isVerify(instructor.getUser().getIsVerify())
                    .createdAt(instructor.getUser().getCreatedAt())
                    .updatedAt(instructor.getUser().getUpdatedAt())
                    .build();

            InstructorDetailResponse instructorDetailResponse = InstructorDetailResponse.builder()
                    .instructor(instructorResponse)
                    .userResponse(userResponse)
                    .build();

            instructorDetailResponses.add(instructorDetailResponse);
        });

        return instructorDetailResponses;
    }

    @Transactional
    @Override
    public UserResponse updateInstructorName(String instructorId, InstructorBiodataRequest instructorBiodataRequest) {
        log.info("Find instructor by id");
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new DataNotFoundException("Instructor not found"));
        log.info("Successfully find instructor by id");
        User user = instructor.getUser();
        user.setInstructorName(instructorBiodataRequest.getInstructorName());
        user.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        userRepository.save(user);
        log.info("Successfully update instructor name with id {} ", instructorId);

        Set<RoleResponse> roleResponses = new HashSet<>();
        instructor.getUser().getRoles().stream()
                .forEach(role -> {
                    RoleResponse roleResponse = RoleResponse.builder()
                            .id(role.getId())
                            .role(role.getName().name())
                            .build();
                    roleResponses.add(roleResponse);
                });
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(roleResponses)
                .imageUrl(user.getImageUrl())
                .instructorName(user.getInstructorName())
                .caption(user.getCaption())
                .biography(user.getBiography())
                .deleted(user.getDeleted())
                .isVerify(user.getIsVerify())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
