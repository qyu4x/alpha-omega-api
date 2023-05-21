package com.alphaomega.alphaomegarestfulapi.service.implementation;


import com.alphaomega.alphaomegarestfulapi.entity.ERole;
import com.alphaomega.alphaomegarestfulapi.entity.Role;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.entity.UserSocialMedia;
import com.alphaomega.alphaomegarestfulapi.exception.DataAlreadyExistsException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.exception.FailedUploadFileException;
import com.alphaomega.alphaomegarestfulapi.exception.InvalidOtpException;
import com.alphaomega.alphaomegarestfulapi.payload.request.*;
import com.alphaomega.alphaomegarestfulapi.payload.response.*;
import com.alphaomega.alphaomegarestfulapi.repository.RoleRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserSocialMediaRepository;
import com.alphaomega.alphaomegarestfulapi.security.configuration.PasswordEncoderConfiguration;
import com.alphaomega.alphaomegarestfulapi.security.service.UserDetailsImpl;
import com.alphaomega.alphaomegarestfulapi.security.util.JwtUtils;
import com.alphaomega.alphaomegarestfulapi.security.util.OtpUtils;
import com.alphaomega.alphaomegarestfulapi.service.FirebaseCloudStorageService;
import com.alphaomega.alphaomegarestfulapi.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.TransactionScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private UserSocialMediaRepository userSocialMediaRepository;

    private RoleRepository roleRepository;

    private FirebaseCloudStorageService  firebaseCloudStorageService;

    private PasswordEncoderConfiguration passwordEncoderConfiguration;

    private JwtUtils jwtUtils;

    private AuthenticationManager authenticationManager;

    private OtpUtils otpUtils;

    public UserServiceImpl(UserRepository userRepository, UserSocialMediaRepository userSocialMediaRepository, RoleRepository roleRepository, FirebaseCloudStorageService firebaseCloudStorageService, PasswordEncoderConfiguration passwordEncoderConfiguration, JwtUtils jwtUtils, AuthenticationManager authenticationManager, OtpUtils otpUtils) {
        this.userRepository = userRepository;
        this.userSocialMediaRepository = userSocialMediaRepository;
        this.roleRepository = roleRepository;
        this.firebaseCloudStorageService = firebaseCloudStorageService;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.otpUtils = otpUtils;
    }

    @Override
    public SignupResponse signup(SignupRequest signupRequest) throws MessagingException {
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(exception -> {
                    throw new  DataAlreadyExistsException(String.format("Account with %s email is available", signupRequest.getEmail()));
                });

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new DataNotFoundException("ROLE_USER not found"));

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        User user = User.builder()
                .id("xx-".concat(UUID.randomUUID().toString()))
                .fullName(signupRequest.getFullName())
                .email(signupRequest.getEmail())
                .password(passwordEncoderConfiguration.passwordEncoder().encode(signupRequest.getPassword()))
                .roles(userRoles)
                .deleted(false)
                .isVerify(false)
                .otp(otpUtils.generateOtp())
                .expirationTime(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime().plusMinutes(5))
                .createdAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime())
                .build();

        userRepository.save(user);
        otpUtils.sendEmail(user.getEmail(), user.getFullName(), "Code Verification", user.getOtp());

        Set<RoleResponse> userRoleResponses = new HashSet<>();
        userRoles.stream().forEach(role -> {
            RoleResponse roleResponse = RoleResponse.builder()
                    .id(role.getId())
                    .role(role.getName().name())
                .build();
            userRoleResponses.add(roleResponse);
        });

        return SignupResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(userRoleResponses)
                .isVerify(user.getIsVerify())
                .expirationTime(user.getExpirationTime())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public SigninResponse signin(SigninRequest signinRequest) {
        User checkIsUserAvailable = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException(String.format("User with email %s not found", signinRequest.getEmail())));
        if(!checkIsUserAvailable.getIsVerify()) {
            throw new BadCredentialsException("Please verify your account first");
        }

        Authentication authentication = authenticate(signinRequest.getEmail(), signinRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(signinRequest.getEmail()).
                orElseThrow(() -> new DataNotFoundException(String.format("User with email %s not found", signinRequest.getEmail())));

        List<RoleResponse> roleResponses = new ArrayList<>();
        user.getRoles().stream()
                .forEach(role -> {
                    RoleResponse roleResponse = RoleResponse.builder()
                            .id(role.getId())
                            .role(role.getName().name())
                            .build();
                    roleResponses.add(roleResponse);
                });

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(jwtUtils.generateJwtToken(authentication))
                .tokenType(jwtUtils.getTokenType())
                .build();

        return SigninResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(roleResponses)
                .jwtResponse(jwtResponse)
                .deleted(user.getDeleted())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public Authentication authenticate(String email, String password) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (DisabledException exception) {
            log.error("User account {} is disabled", email);
            throw new DisabledException("account is disabled");
        } catch (BadCredentialsException exception) {
            log.error("Username or password is wrong for user account {} ", email);
            throw new BadCredentialsException("Email or password is wrong");
        }
    }

    @Override
    @Transactional
    public SignupResponse verifyOtp(OtpVerificationRequest otpVerificationRequest) {
        User user = userRepository.findByEmail(otpVerificationRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException(String.format("User with email %s not found", otpVerificationRequest.getEmail())));

        if (!otpVerificationRequest.getOtpCode().equals(user.getOtp())) {
            log.info("Otp code {} ", user.getOtp());
            log.info("Otp code server {} ", otpVerificationRequest.getOtpCode());
            throw new InvalidOtpException("Otp code does not match");
        }

        if (user.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("Code otp has expired");
        }

        user.setIsVerify(true);
        userRepository.save(user);

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(jwtUtils.generateRegisterJwtToken(user))
                .tokenType(jwtUtils.getTokenType())
                .build();

        Set<RoleResponse> roleResponses = new HashSet<>();
        user.getRoles().stream()
                .forEach(role -> {
                    RoleResponse roleResponse = RoleResponse.builder()
                            .id(role.getId())
                            .role(role.getName().name())
                            .build();
                    roleResponses.add(roleResponse);
                });

        return SignupResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(roleResponses)
                .token(jwtResponse)
                .deleted(user.getDeleted())
                .isVerify(user.getIsVerify())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public SignupResponse refreshVerifyOtp(OtpRefreshCodeRequest otpRefreshCodeRequest) throws MessagingException {
        User user = userRepository.findByEmail(otpRefreshCodeRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException(String.format("User with email %s not found", otpRefreshCodeRequest.getEmail())));

        user.setOtp(otpUtils.generateOtp());
        user.setExpirationTime(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime().plusMinutes(5));
        userRepository.save(user);

        otpUtils.sendEmail(user.getEmail(), user.getFullName(), "AO Verification Code", user.getOtp());

        Set<RoleResponse> userRoleResponses = new HashSet<>();
        user.getRoles().stream()
                .forEach(role -> {
                    RoleResponse roleResponse = RoleResponse.builder()
                            .id(role.getId())
                            .role(role.getName().name())
                            .build();
                    userRoleResponses.add(roleResponse);
                });

        return SignupResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(userRoleResponses)
                .isVerify(user.getIsVerify())
                .expirationTime(user.getExpirationTime())
                .createdAt(user.getCreatedAt())
                .build();

    }

    @Transactional
    @Override
    public UserResponse update(UpdateUserRequest updateUserRequest, String userId) {
        log.info("Perform the update process for user");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        UserSocialMedia userSocialMedia = userSocialMediaRepository.findByUserId(userId);
        if (userSocialMedia == null) {
            log.info("this");
            userSocialMedia = UserSocialMedia.builder()
                    .id("sc-".concat(UUID.randomUUID().toString()))
                    .user(user)
                    .webUrl(updateUserRequest.getWebUrl())
                    .facebook(updateUserRequest.getFacebookUrl())
                    .linkedinUrl(updateUserRequest.getLinkedinUrl())
                    .youtubeUrl(updateUserRequest.getYoutubeUrl())
                    .createdAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime())
                    .updatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime())
                    .build();
        } else {
            userSocialMedia.setWebUrl(updateUserRequest.getWebUrl());
            userSocialMedia.setFacebook(updateUserRequest.getFacebookUrl());
            userSocialMedia.setLinkedinUrl(updateUserRequest.getLinkedinUrl());
            userSocialMedia.setYoutubeUrl(updateUserRequest.getYoutubeUrl());
            userSocialMedia.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        }

        user.setFullName(updateUserRequest.getFullName());
        user.setCaption(updateUserRequest.getCaption());
        user.setBiography(updateUserRequest.getBiography());
        user.setUserSocialMedia(userSocialMedia);
        user.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        userRepository.save(user);
        log.info("Successfully update user information");

        Set<RoleResponse> userRoleResponses = new HashSet<>();
        user.getRoles().stream()
                .forEach(role -> {
                    RoleResponse roleResponse = RoleResponse.builder()
                            .id(role.getId())
                            .role(role.getName().name())
                            .build();
                    userRoleResponses.add(roleResponse);
                });


        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(userRoleResponses)
                .imageUrl(user.getImageUrl())
                .instructorName(user.getInstructorName())
                .caption(user.getCaption())
                .biography(user.getBiography())
                .deleted(user.getDeleted())
                .isVerify(user.getIsVerify())
                .webUrl(user.getUserSocialMedia().getWebUrl())
                .facebookUrl(user.getUserSocialMedia().getFacebook())
                .linkedinUrl(user.getUserSocialMedia().getLinkedinUrl())
                .youtubeUrl(user.getUserSocialMedia().getYoutubeUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public UserResponse updateProfile(MultipartFile multipartFile, String userId) {
        log.info("Perform the update profile process for user");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        if (multipartFile.isEmpty()) {
            throw new FailedUploadFileException("Image cannot be empty");
        }
        String imageUrl = firebaseCloudStorageService.doUploadImageFile(multipartFile);
        user.setImageUrl(imageUrl);
        userRepository.save(user);
        log.info("Successfully update user profile");

        Set<RoleResponse> userRoleResponses = new HashSet<>();
        user.getRoles().stream()
                .forEach(role -> {
                    RoleResponse roleResponse = RoleResponse.builder()
                            .id(role.getId())
                            .role(role.getName().name())
                            .build();
                    userRoleResponses.add(roleResponse);
                });

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(userRoleResponses)
                .imageUrl(user.getImageUrl())
                .instructorName(user.getInstructorName())
                .caption(user.getCaption())
                .biography(user.getBiography())
                .deleted(user.getDeleted())
                .isVerify(user.getIsVerify())
                .webUrl(user.getUserSocialMedia().getWebUrl())
                .facebookUrl(user.getUserSocialMedia().getFacebook())
                .linkedinUrl(user.getUserSocialMedia().getLinkedinUrl())
                .youtubeUrl(user.getUserSocialMedia().getYoutubeUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public UserResponse findById(String userId) {
        log.info("Perform find user by id");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        log.info("Successfully get user by id");

        Set<RoleResponse> userRoleResponses = new HashSet<>();
        user.getRoles().stream()
                .forEach(role -> {
                    RoleResponse roleResponse = RoleResponse.builder()
                            .id(role.getId())
                            .role(role.getName().name())
                            .build();
                    userRoleResponses.add(roleResponse);
                });

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(userRoleResponses)
                .imageUrl(user.getImageUrl())
                .instructorName(user.getInstructorName())
                .caption(user.getCaption())
                .biography(user.getBiography())
                .deleted(user.getDeleted())
                .isVerify(user.getIsVerify())
                .webUrl(user.getUserSocialMedia().getWebUrl())
                .facebookUrl(user.getUserSocialMedia().getFacebook())
                .linkedinUrl(user.getUserSocialMedia().getLinkedinUrl())
                .youtubeUrl(user.getUserSocialMedia().getYoutubeUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public Boolean delete(String userId) {
        log.info("Perform delete user by id");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        roleRepository.deleteUserRoleByUserId(userId);
        userRepository.delete(user);
        log.info("Successfully delete user by id");
        return true;
    }
}
