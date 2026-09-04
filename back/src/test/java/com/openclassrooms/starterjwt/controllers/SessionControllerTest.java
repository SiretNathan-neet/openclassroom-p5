package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private final Session mockSession = Session.builder()
            .id(1L).name("Yoga").date(new Date()).description("desc").users(Collections.emptyList()).build();

    private final SessionDto mockSessionDto = new SessionDto(
            1L, "Yoga", new Date(), 1L, "desc", Collections.emptyList(), null, null
    );

    @Test
    @WithMockUser
    void shouldReturnSessionById() throws Exception {
        when(sessionService.getById(1L)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenSessionNotFound() throws Exception {
        when(sessionService.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/session/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenGetIdIsNotANumber() throws Exception {
        mockMvc.perform(get("/api/session/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnAllSessions() throws Exception {
        List<Session> sessions = Arrays.asList(mockSession);
        List<SessionDto> dtos = Arrays.asList(mockSessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(dtos);

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void shouldCreateSession() throws Exception {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(mockSession);
        when(sessionService.create(mockSession)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockSessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga"));
    }

    @Test
    @WithMockUser
    void shouldUpdateSession() throws Exception {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(mockSession);
        when(sessionService.update(eq(1L), any(Session.class))).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockSessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga"));
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenUpdateIdIsNotANumber() throws Exception {
        mockMvc.perform(put("/api/session/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockSessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldDeleteSessionWhenFound() throws Exception {
        when(sessionService.getById(1L)).thenReturn(mockSession);

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenDeletingUnknownSession() throws Exception {
        when(sessionService.getById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/session/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenDeleteIdIsNotANumber() throws Exception {
        mockMvc.perform(delete("/api/session/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldParticipate() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/2"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).participate(1L, 2L);
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenParticipateIdsAreNotNumbers() throws Exception {
        mockMvc.perform(post("/api/session/abc/participate/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldNoLongerParticipate() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/2"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).noLongerParticipate(1L, 2L);
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenNoLongerParticipateIdsAreNotNumbers() throws Exception {
        mockMvc.perform(delete("/api/session/abc/participate/2"))
                .andExpect(status().isBadRequest());
    }
}