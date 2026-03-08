package nl.mpdev.project_manager_backend.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import nl.mpdev.project_manager_backend.models.Authority;
import nl.mpdev.project_manager_backend.models.User;
import nl.mpdev.project_manager_backend.repositories.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final String adminEmail;

  public UserService(UserRepository userRepository, @Value("${app.admin.email}") String adminEmail) {
    this.userRepository = userRepository;
    this.adminEmail = adminEmail;
  }

  public User registerNewUser(User entity) {
    Authority.AuthorityBuilder authorityBuilder = Authority.builder();
    if (entity.getEmail() != null) {
      authorityBuilder.username(entity.getEmail());
    }
    // DEFAULT USER
    Set<Authority> authorities = new HashSet<>();
    if (entity.getEmail() != null && entity.getEmail().equalsIgnoreCase(adminEmail)) {
      authorities.add(authorityBuilder.authority("ADMIN").build());
    } else {
      authorities.add(authorityBuilder.authority("DEFAULT_USER").build());
    }

    entity = entity.toBuilder()
        .authorities(authorities)
        .build();

    return userRepository.save(entity);

  }

  public boolean checkIfUserExist(String email) {
    if (userRepository.findByEmail(email).isPresent()) {
      System.out.println("this is already found");
      return false;
    }
    return true;
  }

  public Set<String> checkForRoles(String email) {
    return userRepository.findByEmail(email)
        .map(User::getAuthorities)
        .filter(authorities -> !authorities.isEmpty())
        .map(authorities -> authorities.stream()
            .map(Authority::getAuthority)
            .collect(Collectors.toSet()))
        .orElseGet(() -> Collections.singleton("DEFAULT_USER"));
  }
}
