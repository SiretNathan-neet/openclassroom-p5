package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.models.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    void shouldBuildSessionWithBuilder() {
        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder().id(1L).lastName("Delahaye").firstName("Margot").build();

        Session session = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(date)
                .description("A relaxing session")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, session.getId());
        assertEquals("Yoga session", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("A relaxing session", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertNotNull(session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @Test
    void shouldConstructSessionWithNoArgsConstructor() {
        Session session = new Session();

        assertNull(session.getId());
    }

    @Test
    void shouldSetFieldsWithChainedSetters() {
        Session session = new Session().setId(1L).setName("Yoga session");

        assertEquals(1L, session.getId());
        assertEquals("Yoga session", session.getName());
    }

    @Test
    void shouldBeEqualWhenSameId() {
        Session session1 = Session.builder().id(1L).name("Yoga").description("desc1").build();
        Session session2 = Session.builder().id(1L).name("Pilates").description("desc2").build();

        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        Session session1 = Session.builder().id(1L).name("Yoga").build();
        Session session2 = Session.builder().id(2L).name("Yoga").build();

        assertNotEquals(session1, session2);
    }

    @Test
    void shouldNotBeEqualToNullOrOtherType() {
        Session session = Session.builder().id(1L).name("Yoga").build();

        assertNotEquals(null, session);
        assertNotEquals("not a session", session);
    }

    @Test
    void shouldGenerateNonNullToString() {
        Session session = Session.builder().id(1L).name("Yoga session").description("desc").build();

        assertNotNull(session.toString());
        assertTrue(session.toString().contains("Yoga session"));
    }

        @Test
    void shouldConstructSessionWithAllArgsConstructor() {
        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder().id(1L).lastName("Delahaye").firstName("Margot").build();
        ArrayList<User> users = new ArrayList<>();

        Session session = new Session(1L, "Yoga session", date, "desc", teacher, users, now, now);

        assertEquals(1L, session.getId());
        assertEquals("Yoga session", session.getName());
        assertEquals(teacher, session.getTeacher());
    }

    @Test
    void shouldSetAllFieldsIndividually() {
        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder().id(1L).lastName("Delahaye").firstName("Margot").build();
        ArrayList<User> users = new ArrayList<>();

        Session session = new Session();
        session.setId(1L);
        session.setName("Yoga session");
        session.setDate(date);
        session.setDescription("desc");
        session.setTeacher(teacher);
        session.setUsers(users);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        assertEquals(1L, session.getId());
        assertEquals("Yoga session", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("desc", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @Test
    void shouldBeEqualWhenBothIdsAreNull() {
        Session session1 = new Session();
        Session session2 = new Session();

        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenOneIdIsNullAndOtherIsNot() {
        Session session1 = new Session(); // id null
        Session session2 = Session.builder().id(1L).name("Yoga").build();

        assertNotEquals(session1, session2);
        assertNotEquals(session2, session1);
    }
}