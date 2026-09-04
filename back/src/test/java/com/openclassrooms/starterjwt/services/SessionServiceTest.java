package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void shouldCreateSession() {
        Session session = Session.builder().id(1L).name("Yoga").build();
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertEquals(session, result);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void shouldDeleteSessionById() {
        sessionService.delete(1L);

        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnAllSessions() {
        Session session1 = Session.builder().id(1L).name("Yoga").build();
        Session session2 = Session.builder().id(2L).name("Pilates").build();
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session1, session2));

        List<Session> result = sessionService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnSessionById() {
        Session session = Session.builder().id(1L).name("Yoga").build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertNotNull(result);
        assertEquals(session, result);
    }

    @Test
    void shouldReturnNullWhenSessionNotFound() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(99L);

        assertNull(result);
    }

    @Test
    void shouldUpdateSession() {
        Session session = Session.builder().name("Yoga").build();
        Session savedSession = Session.builder().id(1L).name("Yoga").build();
        when(sessionRepository.save(any(Session.class))).thenReturn(savedSession);

        Session result = sessionService.update(1L, session);

        assertEquals(1L, session.getId());
        assertEquals(savedSession, result);
    }

    @Test
    void shouldParticipate() {
        User user = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").admin(false).password("pass").build();
        Session session = Session.builder().id(1L).name("Yoga").users(new ArrayList<>()).build();

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        sessionService.participate(1L, 1L);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void shouldThrowNotFoundWhenParticipateWithUnknownSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(
                User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").admin(false).password("pass").build()
            ));

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void shouldThrowNotFoundWhenParticipateWithUnknownUser() {
        Session session = Session.builder().id(1L).name("Yoga").users(new ArrayList<>()).build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void shouldThrowBadRequestWhenAlreadyParticipating() {
        User user = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").admin(false).password("pass").build();
        Session session = Session.builder().id(1L).name("Yoga").users(new ArrayList<>(Collections.singletonList(user))).build();

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void shouldNoLongerParticipate() {
        User user = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").admin(false).password("pass").build();
        Session session = Session.builder().id(1L).name("Yoga").users(new ArrayList<>(Collections.singletonList(user))).build();

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 1L);

        assertTrue(session.getUsers().isEmpty());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void shouldThrowNotFoundWhenNoLongerParticipateWithUnknownSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void shouldThrowBadRequestWhenNotParticipating() {
        Session session = Session.builder().id(1L).name("Yoga").users(new ArrayList<>()).build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }
}