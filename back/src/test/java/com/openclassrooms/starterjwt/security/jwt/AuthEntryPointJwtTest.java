package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

class AuthEntryPointJwtTest {

    @Test
    void shouldReturnUnauthorizedResponseWithJsonBody() throws Exception {
        AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/session");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException exception = new BadCredentialsException("Bad credentials");

        authEntryPointJwt.commence(request, response, exception);

        assertEquals(401, response.getStatus());
        assertEquals("application/json", response.getContentType());

        String content = response.getContentAsString();
        assertTrue(content.contains("\"status\":401"));
        assertTrue(content.contains("\"error\":\"Unauthorized\""));
        assertTrue(content.contains("Bad credentials"));
        assertTrue(content.contains("/api/session"));
    }
}