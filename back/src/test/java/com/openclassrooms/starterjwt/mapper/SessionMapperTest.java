package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    private SessionMapper sessionMapper;

    @BeforeEach
    void setUp() {
        sessionMapper = new SessionMapperImpl();
        // Champs package-private de SessionMapper : injection manuelle car pas de @InjectMocks
        // possible sur une instance créée avec "new" plutôt que par Spring
        sessionMapper.teacherService = teacherService;
        sessionMapper.userService = userService;
    }

    @Test
    void shouldMapDtoToEntity_withTeacherAndUsers() {
        Teacher teacher = Teacher.builder().id(1L).firstName("Margot").lastName("Delahaye").build();
        User user1 = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").admin(false).password("pass").build();
        User user2 = User.builder().id(2L).email("b@studio.com").lastName("Smith").firstName("Jane").admin(false).password("pass").build();

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        SessionDto dto = new SessionDto(
                1L, "Yoga session", new Date(), 1L, "A relaxing session",
                Arrays.asList(1L, 2L), null, null
        );

        Session entity = sessionMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(teacher, entity.getTeacher());
        assertEquals(2, entity.getUsers().size());
        assertEquals(user1, entity.getUsers().get(0));
        assertEquals(user2, entity.getUsers().get(1));
    }

    @Test
    void shouldMapDtoToEntity_withNullTeacherId() {
        SessionDto dto = new SessionDto(
                1L, "Yoga session", new Date(), null, "A relaxing session",
                Collections.emptyList(), null, null
        );

        Session entity = sessionMapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getTeacher());
    }

    @Test
    void shouldMapDtoToEntity_withNullUsersList() {
        SessionDto dto = new SessionDto(
                1L, "Yoga session", new Date(), null, "A relaxing session",
                null, null, null
        );

        Session entity = sessionMapper.toEntity(dto);

        assertNotNull(entity);
        assertNotNull(entity.getUsers());
        assertTrue(entity.getUsers().isEmpty());
    }

    @Test
    void shouldMapEntityToDto_withTeacherAndUsers() {
        Teacher teacher = Teacher.builder().id(1L).firstName("Margot").lastName("Delahaye").build();
        User user1 = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").admin(false).password("pass").build();
        User user2 = User.builder().id(2L).email("b@studio.com").lastName("Smith").firstName("Jane").admin(false).password("pass").build();

        Session entity = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(new Date())
                .description("A relaxing session")
                .teacher(teacher)
                .users(Arrays.asList(user1, user2))
                .build();

        SessionDto dto = sessionMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());
        assertEquals(teacher.getId(), dto.getTeacher_id());
        assertEquals(2, dto.getUsers().size());
        assertEquals(user1.getId(), dto.getUsers().get(0));
        assertEquals(user2.getId(), dto.getUsers().get(1));
    }

    @Test
    void shouldMapEntityToDto_withNullTeacher() {
        Session entity = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(new Date())
                .description("A relaxing session")
                .teacher(null)
                .users(Collections.emptyList())
                .build();

        SessionDto dto = sessionMapper.toDto(entity);

        assertNotNull(dto);
        assertNull(dto.getTeacher_id());
    }

    @Test
    void shouldMapEntityToDto_withNullUsersList() {
        Session entity = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(new Date())
                .description("A relaxing session")
                .users(null)
                .build();

        SessionDto dto = sessionMapper.toDto(entity);

        assertNotNull(dto);
        assertNotNull(dto.getUsers());
        assertTrue(dto.getUsers().isEmpty());
    }

    @Test
    void shouldMapDtoListToEntityList() {
        SessionDto dto1 = new SessionDto(1L, "Yoga", new Date(), null, "desc1", Collections.emptyList(), null, null);
        SessionDto dto2 = new SessionDto(2L, "Pilates", new Date(), null, "desc2", Collections.emptyList(), null, null);
        List<SessionDto> dtoList = Arrays.asList(dto1, dto2);

        List<Session> entityList = sessionMapper.toEntity(dtoList);

        assertNotNull(entityList);
        assertEquals(2, entityList.size());
        assertEquals(dto1.getName(), entityList.get(0).getName());
        assertEquals(dto2.getName(), entityList.get(1).getName());
    }

    @Test
    void shouldMapEntityListToDtoList() {
        Session entity1 = Session.builder().id(1L).name("Yoga").date(new Date()).description("desc1").users(Collections.emptyList()).build();
        Session entity2 = Session.builder().id(2L).name("Pilates").date(new Date()).description("desc2").users(Collections.emptyList()).build();
        List<Session> entityList = Arrays.asList(entity1, entity2);

        List<SessionDto> dtoList = sessionMapper.toDto(entityList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(entity1.getName(), dtoList.get(0).getName());
        assertEquals(entity2.getName(), dtoList.get(1).getName());
    }

    @Test
    void shouldReturnNullWhenMappingNullDtoToEntity() {
        assertNull(sessionMapper.toEntity((SessionDto) null));
    }

    @Test
    void shouldReturnNullWhenMappingNullEntityToDto() {
        assertNull(sessionMapper.toDto((Session) null));
    }
}