package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    private final String jwtSecret = "testSecretKeyForJwtTestingPurposesOnly";
    private final int jwtExpirationMs = 86400000;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void shouldGenerateJwtToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L).username("test@studio.com").build();
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractUsernameFromToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L).username("test@studio.com").build();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("test@studio.com", username);
    }

    @Test
    void shouldValidateCorrectToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L).username("test@studio.com").build();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void shouldReturnFalseForMalformedToken() {
        assertFalse(jwtUtils.validateJwtToken("this.is.not.a.valid.jwt"));
    }

    @Test
    void shouldReturnFalseForExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject("test@studio.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 20000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    void shouldReturnFalseForTokenWithWrongSignature() {
        String tokenWithWrongSecret = Jwts.builder()
                .setSubject("test@studio.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(SignatureAlgorithm.HS512, "aCompletelyDifferentSecretKey")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(tokenWithWrongSecret));
    }

    @Test
    void shouldReturnFalseForEmptyToken() {
        assertFalse(jwtUtils.validateJwtToken(""));
    }
}