package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roadmap_detail")
public class RoadmapDetail {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "roadmap_category_id")
    private RoadmapCategory roadmapCategory;

    private String name;

    @URL
    private String sourceUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
