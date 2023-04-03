package com.alphaomega.alphaomegarestfulapi.service.implementation;


import com.alphaomega.alphaomegarestfulapi.entity.ERole;
import com.alphaomega.alphaomegarestfulapi.entity.Role;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.exception.DataAlreadyExistsException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.request.SignupRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.JwtResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.RoleResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.SignupResponse;
import com.alphaomega.alphaomegarestfulapi.repository.RoleRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserRepository;
import com.alphaomega.alphaomegarestfulapi.security.configuration.PasswordEncoderConfiguration;
import com.alphaomega.alphaomegarestfulapi.security.util.JwtUtils;
import com.alphaomega.alphaomegarestfulapi.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoderConfiguration passwordEncoderConfiguration;

    private JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoderConfiguration passwordEncoderConfiguration, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public SignupResponse signup(SignupRequest signupRequest) {
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(exception -> {
                    throw new  DataAlreadyExistsException(String.format("Account with %s email is available", signupRequest.getEmail()));
                });

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new DataNotFoundException("ROLE_USER not found"));

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(jwtUtils.generateRegisterJwtToken(signupRequest))
                .tokenType(jwtUtils.getTokenType())
                .build();

        User user = User.builder()
                .id("xx-".concat(UUID.randomUUID().toString()))
                .fullName(signupRequest.getFullName())
                .email(signupRequest.getEmail())
                .password(passwordEncoderConfiguration.passwordEncoder().encode(signupRequest.getPassword()))
                .roles(userRoles)
                .deleted(false)
                .createdAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime())
                .build();

        userRepository.save(user);

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
                .token(jwtResponse)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
