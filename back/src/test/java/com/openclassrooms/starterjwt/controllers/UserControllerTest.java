package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private final User mockUser = User.builder()
            .id(1L)
            .email("test@studio.com")
            .lastName("Doe")
            .firstName("John")
            .password("pass")
            .admin(false)
            .build();

    private final UserDto mockUserDto = new UserDto(
            1L, "test@studio.com", "Doe", "John", false, "pass", null, null
    );

    @Test
    @WithMockUser(username = "test@studio.com")
    void shouldReturnUserById() throws Exception {
        when(userService.findById(1L)).thenReturn(mockUser);
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@studio.com"));
    }

    @Test
    @WithMockUser(username = "test@studio.com")
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(userService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/user/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@studio.com")
    void shouldReturn400WhenIdIsNotANumber() throws Exception {
        mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@studio.com")
    void shouldDeleteUserWhenAuthorized() throws Exception {
        when(userService.findById(1L)).thenReturn(mockUser);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(username = "another@studio.com")
    void shouldReturn401WhenDeletingAnotherUsersAccount() throws Exception {
        when(userService.findById(1L)).thenReturn(mockUser);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).delete(anyLong());
    }

    @Test
    @WithMockUser(username = "test@studio.com")
    void shouldReturn404WhenDeletingUnknownUser() throws Exception {
        when(userService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/user/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@studio.com")
    void shouldReturn400WhenDeletingWithInvalidId() throws Exception {
        mockMvc.perform(delete("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }
}