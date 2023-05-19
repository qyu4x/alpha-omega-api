package com.alphaomega.alphaomegarestfulapi.repository;

import com.alphaomega.alphaomegarestfulapi.entity.UserSocialMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSocialMediaRepository extends JpaRepository<UserSocialMedia, String> {

    UserSocialMedia findByUserId(String id);

}
