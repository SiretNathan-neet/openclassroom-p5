package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = new TeacherMapperImpl();
    }

    @Test
    void shouldMapDtoToEntity() {
        TeacherDto dto = new TeacherDto(
                1L,
                "Delahaye",
                "Margot",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Teacher entity = teacherMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getFirstName(), entity.getFirstName());
    }

    @Test
    void shouldMapEntityToDto() {
        Teacher entity = Teacher.builder()
                .id(1L)
                .lastName("Delahaye")
                .firstName("Margot")
                .build();

        TeacherDto dto = teacherMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getFirstName(), dto.getFirstName());
    }

    @Test
    void shouldMapDtoListToEntityList() {
        TeacherDto dto1 = new TeacherDto(1L, "Delahaye", "Margot", LocalDateTime.now(), LocalDateTime.now());
        TeacherDto dto2 = new TeacherDto(2L, "Thiercelin", "Hélène", LocalDateTime.now(), LocalDateTime.now());
        List<TeacherDto> dtoList = Arrays.asList(dto1, dto2);

        List<Teacher> entityList = teacherMapper.toEntity(dtoList);

        assertNotNull(entityList);
        assertEquals(2, entityList.size());
        assertEquals(dto1.getLastName(), entityList.get(0).getLastName());
        assertEquals(dto2.getLastName(), entityList.get(1).getLastName());
    }

    @Test
    void shouldMapEntityListToDtoList() {
        Teacher entity1 = Teacher.builder().id(1L).lastName("Delahaye").firstName("Margot").build();
        Teacher entity2 = Teacher.builder().id(2L).lastName("Thiercelin").firstName("Hélène").build();
        List<Teacher> entityList = Arrays.asList(entity1, entity2);

        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(entity1.getLastName(), dtoList.get(0).getLastName());
        assertEquals(entity2.getLastName(), dtoList.get(1).getLastName());
    }

    @Test
    void shouldReturnNullWhenMappingNullDtoToEntity() {
        assertNull(teacherMapper.toEntity((TeacherDto) null));
    }

    @Test
    void shouldReturnNullWhenMappingNullEntityToDto() {
        assertNull(teacherMapper.toDto((Teacher) null));
    }
}