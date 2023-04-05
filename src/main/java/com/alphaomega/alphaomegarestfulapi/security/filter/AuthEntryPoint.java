package com.alphaomega.alphaomegarestfulapi.security.filter;

import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        WebResponse<Map<String, ?>> webResponse = new WebResponse<>();
        webResponse.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        webResponse.setStatus(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        webResponse.setData(Map.of(
                "message", authException.getMessage(),
                "path", request.getServletPath()
        ));

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), webResponse);
    }
}
