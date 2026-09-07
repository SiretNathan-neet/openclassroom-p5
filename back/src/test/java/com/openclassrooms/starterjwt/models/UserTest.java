package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldBuildUserWithBuilder() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test@studio.com")
                .lastName("Doe")
                .firstName("John")
                .password("pass")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("test@studio.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("pass", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldConstructUserWithAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "test@studio.com", "Doe", "John", "pass", true, now, now);

        assertEquals(1L, user.getId());
        assertEquals("test@studio.com", user.getEmail());
    }

    @Test
    void shouldConstructUserWithRequiredArgsConstructor() {
        User user = new User("test@studio.com", "Doe", "John", "pass", false);

        assertEquals("test@studio.com", user.getEmail());
        assertFalse(user.isAdmin());
    }

    @Test
    void shouldSetFieldsWithChainedSetters() {
        User user = new User().setId(1L).setEmail("test@studio.com");

        assertEquals(1L, user.getId());
        assertEquals("test@studio.com", user.getEmail());
    }

    @Test
    void shouldBeEqualWhenSameId() {
        User user1 = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").password("p1").admin(false).build();
        User user2 = User.builder().id(1L).email("b@studio.com").lastName("Smith").firstName("Jane").password("p2").admin(true).build();

        // @EqualsAndHashCode(of = {"id"}) : seuls les id comptent, même si le reste diffère
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        User user1 = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").password("p1").admin(false).build();
        User user2 = User.builder().id(2L).email("a@studio.com").lastName("Doe").firstName("John").password("p1").admin(false).build();

        assertNotEquals(user1, user2);
    }

    @Test
    void shouldNotBeEqualToNullOrOtherType() {
        User user = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").password("p1").admin(false).build();

        assertNotEquals(null, user);
        assertNotEquals("not a user", user);
    }

    @Test
    void shouldBeEqualToItself() {
        User user = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").password("p1").admin(false).build();

        assertEquals(user, user);
    }

    @Test
    void shouldGenerateNonNullToString() {
        User user = User.builder().id(1L).email("test@studio.com").lastName("Doe").firstName("John").password("p1").admin(false).build();

        assertNotNull(user.toString());
        assertTrue(user.toString().contains("test@studio.com"));
    }

    @Test
    void shouldConstructUserWithNoArgsConstructor() {
        User user = new User();

        assertNull(user.getId());
    }

    @Test
    void shouldSetAllFieldsIndividually() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setEmail("test@studio.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("pass");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("test@studio.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("pass", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldBeEqualWhenBothIdsAreNull() {
        User user1 = new User("a@studio.com", "Doe", "John", "p1", false);
        User user2 = new User("b@studio.com", "Smith", "Jane", "p2", true);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenOneIdIsNullAndOtherIsNot() {
        User user1 = new User("a@studio.com", "Doe", "John", "p1", false); // id null
        User user2 = User.builder().id(1L).email("a@studio.com").lastName("Doe").firstName("John").password("p1").admin(false).build();

        assertNotEquals(user1, user2);
        assertNotEquals(user2, user1);
    }

        @Test
    void shouldThrowExceptionWhenSettingNullEmail() {
        User user = new User();
        assertThrows(NullPointerException.class, () -> user.setEmail(null));
    }

    @Test
    void shouldThrowExceptionWhenSettingNullLastName() {
        User user = new User();
        assertThrows(NullPointerException.class, () -> user.setLastName(null));
    }

    @Test
    void shouldThrowExceptionWhenSettingNullFirstName() {
        User user = new User();
        assertThrows(NullPointerException.class, () -> user.setFirstName(null));
    }

    @Test
    void shouldThrowExceptionWhenSettingNullPassword() {
        User user = new User();
        assertThrows(NullPointerException.class, () -> user.setPassword(null));
    }

    @Test
    void shouldThrowExceptionWhenConstructingWithNullEmail() {
        assertThrows(NullPointerException.class,
                () -> new User(null, "Doe", "John", "pass", false));
    }

    @Test
    void shouldThrowExceptionWhenConstructingWithAllArgsAndNullEmail() {
        LocalDateTime now = LocalDateTime.now();
        assertThrows(NullPointerException.class,
                () -> new User(1L, null, "Doe", "John", "pass", false, now, now));
    }
}