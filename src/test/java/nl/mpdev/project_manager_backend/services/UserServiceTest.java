package nl.mpdev.project_manager_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.mpdev.project_manager_backend.models.Authority;
import nl.mpdev.project_manager_backend.models.User;
import nl.mpdev.project_manager_backend.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private static final String ADMIN_EMAIL = "admin@example.com";

  @Mock
  private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository, ADMIN_EMAIL);
  }

  @Test
  void registerNewUser_AssignsAdminAuthorityWhenEmailMatches() {
    User request = User.builder()
        .email(ADMIN_EMAIL)
        .build();
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User saved = userService.registerNewUser(request);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captor.capture());
    User persisted = captor.getValue();

    assertEquals(saved, persisted);
    assertEquals(1, persisted.getAuthorities().size());
    Authority authority = persisted.getAuthorities().iterator().next();
    assertEquals("ADMIN", authority.getAuthority());
    assertEquals(ADMIN_EMAIL, authority.getUsername());
  }

  @Test
  void registerNewUser_AssignsDefaultRoleForOtherEmails() {
    String email = "user@example.com";
    User request = User.builder()
        .email(email)
        .build();
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User saved = userService.registerNewUser(request);

    Authority authority = saved.getAuthorities().iterator().next();
    assertEquals("DEFAULT_USER", authority.getAuthority());
    assertEquals(email, authority.getUsername());
  }

  @Test
  void checkIfUserExist_ReturnsFalse_WhenRepositoryFindsUser() {
    when(userRepository.findByEmail("user@example.com"))
        .thenReturn(Optional.of(User.builder().build()));

    boolean result = userService.checkIfUserExist("user@example.com");

    assertFalse(result);
    verify(userRepository).findByEmail("user@example.com");
  }

  @Test
  void checkIfUserExist_ReturnsTrue_WhenRepositoryEmpty() {
    when(userRepository.findByEmail("unknown@example.com"))
        .thenReturn(Optional.empty());

    boolean result = userService.checkIfUserExist("unknown@example.com");

    assertTrue(result);
    verify(userRepository).findByEmail("unknown@example.com");
  }

  @Test
  void checkForRoles_ReturnsMappedAuthorities_WhenUserHasAuthorities() {
    Authority admin = Authority.builder()
        .username("user@example.com")
        .authority("ADMIN")
        .build();
    Authority other = Authority.builder()
        .username("user@example.com")
        .authority("DEFAULT_USER")
        .build();
    User user = User.builder()
        .email("user@example.com")
        .authorities(Set.of(admin, other))
        .build();
    when(userRepository.findByEmail("user@example.com"))
        .thenReturn(Optional.of(user));

    Set<String> roles = userService.checkForRoles("user@example.com");

    assertTrue(roles.contains("ADMIN"));
    assertTrue(roles.contains("DEFAULT_USER"));
    assertEquals(2, roles.size());
  }

  @Test
  void checkForRoles_ReturnsDefaultUser_WhenNoUserOrAuthorities() {
    when(userRepository.findByEmail("missing@example.com"))
        .thenReturn(Optional.empty());

    Set<String> missingRoles = userService.checkForRoles("missing@example.com");
    assertEquals(Collections.singleton("DEFAULT_USER"), missingRoles);

    User user = User.builder()
        .email("user@example.com")
        .authorities(Collections.emptySet())
        .build();
    when(userRepository.findByEmail("user@example.com"))
        .thenReturn(Optional.of(user));

    Set<String> emptyRoles = userService.checkForRoles("user@example.com");
    assertEquals(Collections.singleton("DEFAULT_USER"), emptyRoles);
    verify(userRepository).findByEmail(eq("missing@example.com"));
    verify(userRepository).findByEmail(eq("user@example.com"));
    verify(userRepository, never()).save(any());
  }
}
