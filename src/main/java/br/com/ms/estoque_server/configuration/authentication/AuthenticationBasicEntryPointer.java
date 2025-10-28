package br.com.ms.estoque_server.configuration.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationBasicEntryPointer implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String authorization = request.getHeader("Authorization");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (authorizationIsNull(authorization)) {
            forbidden(response);
            return;
        }
        unauthorized(response);
    }

    private boolean authorizationIsNull(String authorization) {
        return StringUtils.isBlank(authorization) || authorization.contains("Basic null");
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        objectMapper.writeValue(response.getWriter(), "unauthorized");
    }

    private void forbidden(HttpServletResponse response) throws IOException {
        response.setStatus(403);
        objectMapper.writeValue(response.getWriter(), "forbidden");
    }
}
