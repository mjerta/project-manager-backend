package nl.mpdev.project_manager_backend.repositories;

import java.util.Optional;
import nl.mpdev.project_manager_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByExternalId(String externalId);

  Optional<User> findByEmail(String email);
}
