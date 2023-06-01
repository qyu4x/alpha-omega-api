package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.CourseCategory;
import com.alphaomega.alphaomegarestfulapi.entity.ERole;
import com.alphaomega.alphaomegarestfulapi.entity.Role;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.exception.DataAlreadyExistsException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.request.SignupRequest;
import com.alphaomega.alphaomegarestfulapi.repository.CourseCategoryRepository;
import com.alphaomega.alphaomegarestfulapi.repository.RoleRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserRepository;
import com.alphaomega.alphaomegarestfulapi.security.configuration.PasswordEncoderConfiguration;
import com.alphaomega.alphaomegarestfulapi.service.InitialService;
import com.alphaomega.alphaomegarestfulapi.service.UserService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class InitialServiceImpl implements InitialService {

    private final static Logger log = LoggerFactory.getLogger(InitialServiceImpl.class);

    @Value("${admin.fullname}")
    private String fullName;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;


    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private CourseCategoryRepository courseCategoryRepository;

    private PasswordEncoderConfiguration passwordEncoderConfiguration;

    public InitialServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CourseCategoryRepository courseCategoryRepository, PasswordEncoderConfiguration passwordEncoderConfiguration) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
    }

    @PostConstruct
    @Override
    public void initAdmin() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFullName(this.fullName);
        signupRequest.setEmail(this.email);
        signupRequest.setPassword(this.password);

        Optional<User> userResponse = userRepository.findByEmail(signupRequest.getEmail());
        if (userResponse.isEmpty()) {
            Set<Role> adminRoles = new HashSet<>(roleRepository.findAll());
            User user = User.builder()
                    .id("adm-".concat(UUID.randomUUID().toString()))
                    .fullName(signupRequest.getFullName())
                    .email(signupRequest.getEmail())
                    .password(passwordEncoderConfiguration.passwordEncoder().encode(signupRequest.getPassword()))
                    .roles(adminRoles)
                    .deleted(false)
                    .isVerify(true)
                    .expirationTime(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime().plusMinutes(5))
                    .createdAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime())
                    .build();

            userRepository.save(user);
        }
        log.info("Successfully create admin account with email {}", signupRequest.getEmail());
    }

    @Override
    @PostConstruct
    public void initCourseCategories() {
        if (courseCategoryRepository.findAll().isEmpty()) {
            List<CourseCategory> courseCategories = new ArrayList<>();

            CourseCategory courseCategoryWebDevelopment = new CourseCategory();
            courseCategoryWebDevelopment.setId("cc".concat(UUID.randomUUID().toString()));
            courseCategoryWebDevelopment.setName("Web Development");
            courseCategoryWebDevelopment.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseCategoryWebDevelopment.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            CourseCategory courseCategoryMobileDevelopment = new CourseCategory();
            courseCategoryMobileDevelopment.setId("cc-".concat(UUID.randomUUID().toString()));
            courseCategoryMobileDevelopment.setName("Mobile Development");
            courseCategoryMobileDevelopment.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseCategoryMobileDevelopment.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            CourseCategory courseCategoryProgrammingLanguage = new CourseCategory();
            courseCategoryProgrammingLanguage.setId("cc-".concat(UUID.randomUUID().toString()));
            courseCategoryProgrammingLanguage.setName("Programming Language");
            courseCategoryProgrammingLanguage.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseCategoryProgrammingLanguage.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            CourseCategory courseCategoryGameDevelopment = new CourseCategory();
            courseCategoryGameDevelopment.setId("cc-".concat(UUID.randomUUID().toString()));
            courseCategoryGameDevelopment.setName("Game Development");
            courseCategoryGameDevelopment.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseCategoryGameDevelopment.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            CourseCategory courseCategoryDatabaseDesign = new CourseCategory();
            courseCategoryDatabaseDesign.setId("cc-".concat(UUID.randomUUID().toString()));
            courseCategoryDatabaseDesign.setName("Database Design & Development");
            courseCategoryDatabaseDesign.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseCategoryDatabaseDesign.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            CourseCategory courseCategorySoftwareTesting= new CourseCategory();
            courseCategorySoftwareTesting.setId("cc-".concat(UUID.randomUUID().toString()));
            courseCategorySoftwareTesting.setName("Software Testing");
            courseCategorySoftwareTesting.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            courseCategorySoftwareTesting.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            courseCategories.add(courseCategoryWebDevelopment);
            courseCategories.add(courseCategoryMobileDevelopment);
            courseCategories.add(courseCategoryProgrammingLanguage);
            courseCategories.add(courseCategoryGameDevelopment);
            courseCategories.add(courseCategorySoftwareTesting);

            courseCategoryRepository.saveAll(courseCategories);
            log.info("Successfully save all course categories");
        }

    }

}