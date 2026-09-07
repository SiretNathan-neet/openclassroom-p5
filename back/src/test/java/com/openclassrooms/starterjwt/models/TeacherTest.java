package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void shouldBuildTeacherWithBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Delahaye")
                .firstName("Margot")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, teacher.getId());
        assertEquals("Delahaye", teacher.getLastName());
        assertEquals("Margot", teacher.getFirstName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    void shouldConstructTeacherWithAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher(1L, "Delahaye", "Margot", now, now);

        assertEquals(1L, teacher.getId());
        assertEquals("Delahaye", teacher.getLastName());
    }

    @Test
    void shouldConstructTeacherWithNoArgsConstructor() {
        Teacher teacher = new Teacher();

        assertNull(teacher.getId());
    }

    @Test
    void shouldSetFieldsWithChainedSetters() {
        Teacher teacher = new Teacher().setId(1L).setLastName("Delahaye");

        assertEquals(1L, teacher.getId());
        assertEquals("Delahaye", teacher.getLastName());
    }

    @Test
    void shouldBeEqualWhenSameId() {
        Teacher teacher1 = Teacher.builder().id(1L).lastName("Delahaye").firstName("Margot").build();
        Teacher teacher2 = Teacher.builder().id(1L).lastName("Thiercelin").firstName("Hélène").build();

        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        Teacher teacher1 = Teacher.builder().id(1L).lastName("Delahaye").firstName("Margot").build();
        Teacher teacher2 = Teacher.builder().id(2L).lastName("Delahaye").firstName("Margot").build();

        assertNotEquals(teacher1, teacher2);
    }

    @Test
    void shouldNotBeEqualToNullOrOtherType() {
        Teacher teacher = Teacher.builder().id(1L).lastName("Delahaye").firstName("Margot").build();

        assertNotEquals(null, teacher);
        assertNotEquals("not a teacher", teacher);
    }

    @Test
    void shouldGenerateNonNullToString() {
        Teacher teacher = Teacher.builder().id(1L).lastName("Delahaye").firstName("Margot").build();

        assertNotNull(teacher.toString());
        assertTrue(teacher.toString().contains("Delahaye"));
    }

    @Test
    void shouldSetAllFieldsIndividually() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Delahaye");
        teacher.setFirstName("Margot");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertEquals(1L, teacher.getId());
        assertEquals("Delahaye", teacher.getLastName());
        assertEquals("Margot", teacher.getFirstName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    void shouldBeEqualWhenBothIdsAreNull() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();

        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
    }

        @Test
    void shouldNotBeEqualWhenOneIdIsNullAndOtherIsNot() {
        Teacher teacher1 = new Teacher(); // id null
        Teacher teacher2 = Teacher.builder().id(1L).lastName("Delahaye").firstName("Margot").build();

        assertNotEquals(teacher1, teacher2);
        assertNotEquals(teacher2, teacher1);
    }
}