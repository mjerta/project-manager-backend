package nl.mpdev.project_manager_backend.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {

  private final Key signingKey;
  private final long expirationSeconds;

  public JwtTokenService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-seconds}") long expirationSeconds) {
    byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secret);
    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    this.expirationSeconds = expirationSeconds;
  }

  public String generateToken(Authentication authentication) {
    Instant now = Instant.now();
    Map<String, String> googleClaims = this.extractClaims(authentication);

    return Jwts.builder()
        .setSubject(authentication.getName())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
        .claim("roles", "DEFAULT_USER")
        .claim("email", googleClaims.get("email"))
        .claim("first_name", googleClaims.get("first_name"))
        .claim("last_name", googleClaims.get("last_name"))
        .claim("profile_photo", googleClaims.get("profile_photo"))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  private Map<String, String> extractClaims(Authentication authentication) {
    Map<String, String> claims = new HashMap<>();
    Object principal = authentication.getPrincipal();
    if (principal instanceof OidcUser oidcUser) {
      claims.put("email", oidcUser.getEmail());
      claims.put("first_name", oidcUser.getGivenName());
      claims.put("last_name", oidcUser.getFamilyName());
      claims.put("profile_photo", oidcUser.getPicture());
    }
    return claims;
  }

}
