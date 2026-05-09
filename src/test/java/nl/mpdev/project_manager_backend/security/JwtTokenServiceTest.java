package nl.mpdev.project_manager_backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import nl.mpdev.project_manager_backend.models.User;
import nl.mpdev.project_manager_backend.services.UserService;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

  private static final String RAW_SECRET = "jwt-test-secret-key-which-is-long-enough-256";
  private static final String SECRET = Base64.getEncoder().encodeToString(RAW_SECRET.getBytes());

  @Mock
  private UserService userService;

  @Mock
  private Authentication authentication;

  @Mock
  private OidcUser oidcUser;

  private JwtTokenService jwtTokenService;

  @BeforeEach
  void setUp() {
    jwtTokenService = new JwtTokenService(SECRET, 3600L, userService);
    when(authentication.getPrincipal()).thenReturn(oidcUser);
    when(authentication.getName()).thenReturn("principal-id");
    when(oidcUser.getSubject()).thenReturn("external-123");
    when(oidcUser.getEmail()).thenReturn("user@example.com");
    when(oidcUser.getGivenName()).thenReturn("Test");
    when(oidcUser.getFamilyName()).thenReturn("User");
    when(oidcUser.getPicture()).thenReturn("http://example.com/photo.png");
  }

  @Test
  void generateToken_ShouldRegisterNewUserAndEmbedClaims() {
    when(userService.checkIfUserExist("user@example.com")).thenReturn(true);
    when(userService.checkForRoles("user@example.com")).thenReturn(Set.of("ADMIN", "DEFAULT_USER"));

    String token = jwtTokenService.generateToken(authentication);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userService).registerNewUser(userCaptor.capture());
    User captured = userCaptor.getValue();
    assertEquals("external-123", captured.getExternalId());
    assertEquals("user@example.com", captured.getEmail());
    assertEquals("Test", captured.getFirstName());
    assertEquals("User", captured.getLastName());

    Claims claims = parseClaims(token);
    assertEquals("principal-id", claims.getSubject());
    assertEquals("user@example.com", claims.get("email"));
    assertEquals("external-123", claims.get("external_id"));
    @SuppressWarnings("unchecked")
    List<String> authorities = (List<String>) claims.get("authorities");
    assertTrue(authorities.contains("ADMIN"));
    assertTrue(authorities.contains("DEFAULT_USER"));

    Map<String, String> googleClaims = jwtTokenService.getGoogleClaims();
    assertEquals("external-123", googleClaims.get("external_id"));
    assertEquals("user@example.com", googleClaims.get("email"));
  }

  @Test
  void generateToken_ShouldSkipRegistrationWhenUserExists() {
    when(userService.checkIfUserExist("user@example.com")).thenReturn(false);
    when(userService.checkForRoles("user@example.com")).thenReturn(Set.of("DEFAULT_USER"));

    String token = jwtTokenService.generateToken(authentication);

    verify(userService, never()).registerNewUser(any(User.class));
    verify(userService).checkForRoles(eq("user@example.com"));

    Claims claims = parseClaims(token);
    @SuppressWarnings("unchecked")
    List<String> authorities = (List<String>) claims.get("authorities");
    assertEquals(Set.of("DEFAULT_USER"), Set.copyOf(authorities));
    Instant issuedAt = claims.getIssuedAt().toInstant();
    Instant expiresAt = claims.getExpiration().toInstant();
    assertEquals(3600L, Duration.between(issuedAt, expiresAt).getSeconds());
  }

  private Claims parseClaims(String token) {
    Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
