package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldLoadUserByUsername() {
        User user = User.builder()
                .id(1L)
                .email("test@studio.com")
                .lastName("Doe")
                .firstName("John")
                .password("encoded-pass")
                .admin(false)
                .build();

        when(userRepository.findByEmail("test@studio.com")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("test@studio.com");

        assertNotNull(result);
        assertEquals("test@studio.com", result.getUsername());
        assertTrue(result instanceof UserDetailsImpl);
        assertEquals(1L, ((UserDetailsImpl) result).getId());
        assertEquals("Doe", ((UserDetailsImpl) result).getLastName());
        assertEquals("John", ((UserDetailsImpl) result).getFirstName());
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail("unknown@studio.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown@studio.com"));
    }
}