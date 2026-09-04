package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void shouldReturnAllTeachers() {
        Teacher teacher1 = Teacher.builder().id(1L).firstName("Margot").lastName("Delahaye").build();
        Teacher teacher2 = Teacher.builder().id(2L).firstName("Hélène").lastName("Thiercelin").build();
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertEquals(2, result.size());
        assertEquals(teachers, result);
    }

    @Test
    void shouldReturnTeacherById() {
        Teacher teacher = Teacher.builder().id(1L).firstName("Margot").lastName("Delahaye").build();
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(1L);

        assertNotNull(result);
        assertEquals(teacher, result);
    }

    @Test
    void shouldReturnNullWhenTeacherNotFound() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(99L);

        assertNull(result);
    }
}