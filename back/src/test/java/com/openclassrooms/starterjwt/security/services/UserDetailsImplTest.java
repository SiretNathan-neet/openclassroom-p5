package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void shouldBuildUserDetailsWithBuilder() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@studio.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("pass")
                .build();

        assertEquals(1L, userDetails.getId());
        assertEquals("test@studio.com", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertFalse(userDetails.getAdmin());
        assertEquals("pass", userDetails.getPassword());
    }

    @Test
    void shouldReturnEmptyAuthorities() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void shouldReturnTrueForAllAccountStatusFlags() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void shouldBeEqualWhenSameId() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).username("a@studio.com").build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).username("b@studio.com").build();

        assertEquals(user1, user2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(2L).build();

        assertNotEquals(user1, user2);
    }

    @Test
    void shouldBeEqualToItself() {
        UserDetailsImpl user = UserDetailsImpl.builder().id(1L).build();

        assertEquals(user, user);
    }

    @Test
    void shouldNotBeEqualToNull() {
        UserDetailsImpl user = UserDetailsImpl.builder().id(1L).build();

        assertNotEquals(null, user);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        UserDetailsImpl user = UserDetailsImpl.builder().id(1L).build();

        assertNotEquals("not a user details", user);
    }
}