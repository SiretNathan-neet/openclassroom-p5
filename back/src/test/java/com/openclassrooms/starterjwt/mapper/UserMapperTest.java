package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // MapStruct génère UserMapperImpl à la compilation (voir mvn generate-sources)
        userMapper = new UserMapperImpl();
    }

    @Test
    void shouldMapDtoToEntity() {
        UserDto dto = new UserDto(
                1L,
                "test@studio.com",
                "Doe",
                "John",
                true,
                "password123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        User entity = userMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.isAdmin(), entity.isAdmin());
        assertEquals(dto.getPassword(), entity.getPassword());
    }

    @Test
    void shouldMapEntityToDto() {
        User entity = User.builder()
                .id(1L)
                .email("test@studio.com")
                .lastName("Doe")
                .firstName("John")
                .admin(false)
                .password("password123")
                .build();

        UserDto dto = userMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.isAdmin(), dto.isAdmin());
        assertEquals(entity.getPassword(), dto.getPassword());
    }

    @Test
    void shouldMapDtoListToEntityList() {
        UserDto dto1 = new UserDto(1L, "a@studio.com", "Doe", "John", true, "pass1", LocalDateTime.now(), LocalDateTime.now());
        UserDto dto2 = new UserDto(2L, "b@studio.com", "Smith", "Jane", false, "pass2", LocalDateTime.now(), LocalDateTime.now());
        List<UserDto> dtoList = Arrays.asList(dto1, dto2);

        List<User> entityList = userMapper.toEntity(dtoList);

        assertNotNull(entityList);
        assertEquals(2, entityList.size());
        assertEquals(dto1.getEmail(), entityList.get(0).getEmail());
        assertEquals(dto2.getEmail(), entityList.get(1).getEmail());
    }

    @Test
    void shouldMapEntityListToDtoList() {
        User entity1 = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").admin(true).password("pass1").build();
        User entity2 = User.builder().id(2L).email("b@studio.com").lastName("Smith").firstName("Jane").admin(false).password("pass2").build();
        List<User> entityList = Arrays.asList(entity1, entity2);

        List<UserDto> dtoList = userMapper.toDto(entityList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(entity1.getEmail(), dtoList.get(0).getEmail());
        assertEquals(entity2.getEmail(), dtoList.get(1).getEmail());
    }

    @Test
    void shouldReturnNullWhenMappingNullDtoToEntity() {
        assertNull(userMapper.toEntity((UserDto) null));
    }

    @Test
    void shouldReturnNullWhenMappingNullEntityToDto() {
        assertNull(userMapper.toDto((User) null));
    }
}