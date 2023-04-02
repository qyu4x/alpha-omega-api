package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "instructorName")
        })
@SQLDelete(sql = "UPDATE users SET deleted = TRUE WHERE id = ?")
@Where(clause = "deleted=false")
public class User {

    @Id
    private String id;

    @Size(max = 25)
    private String fullName;

    @Size(max = 50)
    private String email;

    @Size(max = 50)
    private String password;

    @URL
    private String imageUrl;

    @Size(max = 50)
    private String instructorName;

    @Column(columnDefinition = "TEXT")
    private String biography;

    private Boolean deleted = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private Instructor instructor;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserSocialMedia userSocialMedia;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = false)
    private List<WishList> wishLists;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ShoppingCart> shoppingCarts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Order> orders;


}

