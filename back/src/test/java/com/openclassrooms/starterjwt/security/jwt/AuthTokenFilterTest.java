package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    private AuthTokenFilter authTokenFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private AuthTokenFilter buildFilter() {
        AuthTokenFilter filter = new AuthTokenFilter();
        ReflectionTestUtils.setField(filter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(filter, "userDetailsService", userDetailsService);
        return filter;
    }

    @Test
    void shouldSetAuthenticationWhenTokenIsValid() throws Exception {
        authTokenFilter = buildFilter();
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L).username("test@studio.com").build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtUtils.validateJwtToken("valid-token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid-token")).thenReturn("test@studio.com");
        when(userDetailsService.loadUserByUsername("test@studio.com")).thenReturn(userDetails);

        authTokenFilter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void shouldNotSetAuthenticationWhenNoAuthorizationHeader() throws Exception {
        authTokenFilter = buildFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldNotSetAuthenticationWhenHeaderHasNoBearerPrefix() throws Exception {
        authTokenFilter = buildFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic sometoken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldNotSetAuthenticationWhenTokenIsInvalid() throws Exception {
        authTokenFilter = buildFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtUtils.validateJwtToken("invalid-token")).thenReturn(false);

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldContinueFilterChainWhenExceptionIsThrown() throws Exception {
        authTokenFilter = buildFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtUtils.validateJwtToken("valid-token")).thenThrow(new RuntimeException("Unexpected error"));

        // Ne doit pas lever d'exception : le filtre catch et continue
        assertDoesNotThrow(() -> authTokenFilter.doFilter(request, response, filterChain));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}