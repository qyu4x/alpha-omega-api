package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponse {

    private String id;

    private String name;

    private String createdAt;

    private String updatedAt;

}
