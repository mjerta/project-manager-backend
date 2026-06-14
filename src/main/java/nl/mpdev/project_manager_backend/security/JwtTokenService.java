package nl.mpdev.project_manager_backend.security;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {

  private final SecretKey secretKey;
  private final long expirationSeconds;

  public JwtTokenService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-seconds:3600}") long expirationSeconds) {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    this.expirationSeconds = expirationSeconds;
  }

  public String generateToken(Authentication authentication) {
    Instant now = Instant.now();
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim("authorities", authorities)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }
}
