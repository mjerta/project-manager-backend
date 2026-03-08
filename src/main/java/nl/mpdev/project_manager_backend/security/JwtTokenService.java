package nl.mpdev.project_manager_backend.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nl.mpdev.project_manager_backend.models.User;
import nl.mpdev.project_manager_backend.services.UserService;

@Service
public class JwtTokenService {

  private final Key signingKey;
  private final long expirationSeconds;
  private Map<String, String> googleClaims;
  private final UserService userService;

  public JwtTokenService(@Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-seconds}") long expirationSeconds, UserService userService) {
    this.userService = userService;
    byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secret);
    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    this.expirationSeconds = expirationSeconds;
  }

  public String generateToken(Authentication authentication) {
    Instant now = Instant.now();
    this.googleClaims = this.extractClaims(authentication);
    // TODO: I need to put the logic in here of creating user and checking for user
    if (this.userService.checkIfUserExist(googleClaims.get("email"))) {

      this.userService.registerNewUser(User.builder()
          .externalId(this.googleClaims.get("external_id"))
          .email(this.googleClaims.get("email"))
          .firstName(this.googleClaims.get("first_name"))
          .lastName(this.googleClaims.get("last_name"))
          .profilePhoto(this.googleClaims.get("profile_photo"))
          .build());
    }

    return Jwts.builder()
        .setSubject(authentication.getName())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
        .claim("external_id", this.googleClaims.get("external_id"))
        .claim("email", this.googleClaims.get("email"))
        .claim("first_name", this.googleClaims.get("first_name"))
        .claim("last_name", this.googleClaims.get("last_name"))
        .claim("profile_photo", this.googleClaims.get("profile_photo"))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  private Map<String, String> extractClaims(Authentication authentication) {
    Map<String, String> claims = new HashMap<>();
    Object principal = authentication.getPrincipal();
    if (principal instanceof OidcUser oidcUser) {
      claims.putIfAbsent("external_id", oidcUser.getSubject());
      claims.putIfAbsent("email", oidcUser.getEmail());
      claims.putIfAbsent("first_name", oidcUser.getGivenName());
      claims.putIfAbsent("last_name", oidcUser.getFamilyName());
      claims.putIfAbsent("profile_photo", oidcUser.getPicture());
    }
    return claims;
  }

  public Map<String, String> getGoogleClaims() {
    return googleClaims;
  }

}
