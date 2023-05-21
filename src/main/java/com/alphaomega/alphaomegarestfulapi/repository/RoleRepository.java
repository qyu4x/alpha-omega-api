package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.ERole;
import com.alphaomega.alphaomegarestfulapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String>{

    Optional<Role> findByName(ERole role);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM user_roles WHERE users_id = :id"

    )
    void deleteUserRoleByUserId(@Param("id") String id);

}
