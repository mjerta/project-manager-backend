package nl.mpdev.project_manager_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import nl.mpdev.project_manager_backend.models.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
