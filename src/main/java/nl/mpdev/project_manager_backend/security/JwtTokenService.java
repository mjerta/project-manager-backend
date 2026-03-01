package nl.mpdev.project_manager_backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
    return Jwts.builder()
        .setSubject(authentication.getName())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }
}
