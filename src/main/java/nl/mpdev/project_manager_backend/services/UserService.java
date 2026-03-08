package nl.mpdev.project_manager_backend.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import nl.mpdev.project_manager_backend.exceptions.GeneralException;
import nl.mpdev.project_manager_backend.models.Authority;
import nl.mpdev.project_manager_backend.models.User;
import nl.mpdev.project_manager_backend.repositories.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User registerNewUser(User entity) {
    Authority.AuthorityBuilder authorityBuilder = Authority.builder();
    if (entity.getEmail() != null) {
      authorityBuilder.username(entity.getEmail());
    }
    // DEFAULT USER
    Set<Authority> authorities = new HashSet<>();
    authorities.add(authorityBuilder.authority("DEFAULT_USER").build());

    entity = entity.toBuilder()
        .authorities(authorities)
        .build();

    return userRepository.save(entity);

  }

  public boolean checkIfUserExist(String email) {
    if (userRepository.findByEmail(email).isPresent()) {
      System.out.println("this is already found");
      // throw new GeneralException("User with email " + entity.getEmail() + " already
      // exists");
      return false;
    }
    return true;
  }
}
