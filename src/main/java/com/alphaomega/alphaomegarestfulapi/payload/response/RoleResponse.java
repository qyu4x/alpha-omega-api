package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {

    private String id;

    private String role;
}
