package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private final Teacher mockTeacher = Teacher.builder()
            .id(1L).lastName("Delahaye").firstName("Margot").build();

    private final TeacherDto mockTeacherDto = new TeacherDto(
            1L, "Delahaye", "Margot", null, null
    );

    @Test
    @WithMockUser
    void shouldReturnTeacherById() throws Exception {
        when(teacherService.findById(1L)).thenReturn(mockTeacher);
        when(teacherMapper.toDto(mockTeacher)).thenReturn(mockTeacherDto);

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Delahaye"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenTeacherNotFound() throws Exception {
        when(teacherService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenIdIsNotANumber() throws Exception {
        mockMvc.perform(get("/api/teacher/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnAllTeachers() throws Exception {
        Teacher teacher2 = Teacher.builder().id(2L).lastName("Thiercelin").firstName("Hélène").build();
        List<Teacher> teachers = Arrays.asList(mockTeacher, teacher2);
        TeacherDto dto2 = new TeacherDto(2L, "Thiercelin", "Hélène", null, null);
        List<TeacherDto> dtos = Arrays.asList(mockTeacherDto, dto2);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(dtos);

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}