package nl.mpdev.project_manager_backend.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String SECRETKEY;

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtTokenService jwtTokenService,
      ObjectMapper objectMapper) throws Exception {

    AuthenticationSuccessHandler successHandler = (request, response, authentication) -> {
      String token = jwtTokenService.generateToken(authentication);
      response.setStatus(HttpStatus.OK.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      objectMapper.writeValue(response.getWriter(), Map.of("accessToken", token));
    };

    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/status").permitAll()
            .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
            .anyRequest().authenticated())
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/login")
            .successHandler(successHandler))
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      Object roles = jwt.getClaim("authorities");

      if (roles instanceof String rolesStr) {
        return List.of(rolesStr.split(","))
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
      } else if (roles instanceof Collection<?> rolesList) {
        return rolesList.stream()
            .map(Object::toString)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
      }

      return List.of();
    });
    return converter;
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(this.SECRETKEY);
    SecretKey spec = new SecretKeySpec(keyBytes, "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(spec).build();
  }
}
