package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.ERole;
import com.alphaomega.alphaomegarestfulapi.entity.Role;
import com.alphaomega.alphaomegarestfulapi.repository.RoleRepository;
import com.alphaomega.alphaomegarestfulapi.service.RoleService;
import jakarta.annotation.PostConstruct;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @PostConstruct
    @Order(1)
    public void create() {
        if (roleRepository.findAll().size() != 3) {
            List<Role> roles = new ArrayList<>();
            roles.add(new Role(UUID.randomUUID().toString().replace("-", ""), ERole.ROLE_USER));
            roles.add(new Role(UUID.randomUUID().toString().replace("-", ""), ERole.ROLE_ADMIN));
            roles.add(new Role(UUID.randomUUID().toString().replace("-", ""), ERole.ROLE_INSTRUCTOR));

            roleRepository.saveAll(roles);

        }
    }
}
