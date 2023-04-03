package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String token;

    private String tokenType;

}
