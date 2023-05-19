package com.alphaomega.alphaomegarestfulapi.service.implementation;


import com.alphaomega.alphaomegarestfulapi.entity.ERole;
import com.alphaomega.alphaomegarestfulapi.entity.Role;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.exception.DataAlreadyExistsException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.exception.InvalidOtpException;
import com.alphaomega.alphaomegarestfulapi.payload.request.OtpRefreshCodeRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.OtpVerificationRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.SigninRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.SignupRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.JwtResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.RoleResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.SigninResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.SignupResponse;
import com.alphaomega.alphaomegarestfulapi.repository.RoleRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserRepository;
import com.alphaomega.alphaomegarestfulapi.security.configuration.PasswordEncoderConfiguration;
import com.alphaomega.alphaomegarestfulapi.security.service.UserDetailsImpl;
import com.alphaomega.alphaomegarestfulapi.security.util.JwtUtils;
import com.alphaomega.alphaomegarestfulapi.security.util.OtpUtils;
import com.alphaomega.alphaomegarestfulapi.service.UserService;
import jakarta.mail.MessagingException;
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

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoderConfiguration passwordEncoderConfiguration;

    private JwtUtils jwtUtils;

    private AuthenticationManager authenticationManager;

    private OtpUtils otpUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoderConfiguration passwordEncoderConfiguration, JwtUtils jwtUtils, AuthenticationManager authenticationManager, OtpUtils otpUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
            throw new InvalidOtpException("Please verify your account first");
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
}
