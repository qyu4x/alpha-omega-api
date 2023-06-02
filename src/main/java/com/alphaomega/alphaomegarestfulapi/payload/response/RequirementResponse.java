package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequirementResponse {

    private String id;

    private String name;

    private String createdAt;

    private String updatedAt;

}
