package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_social_media")
public class UserSocialMedia {

    @Id
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @URL
    private String webUrl;

    @URL
    private String facebook;

    @URL
    private String linkedinUrl;

    @URL
    private String youtubeUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
