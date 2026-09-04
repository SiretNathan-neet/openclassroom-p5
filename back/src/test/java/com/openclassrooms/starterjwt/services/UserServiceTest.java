package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUserById() {
        User user = User.builder().id(1L).email("test@studio.com").lastName("Doe").firstName("John").admin(false).password("pass").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User result = userService.findById(99L);

        assertNull(result);
    }

    @Test
    void shouldDeleteUserById() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}