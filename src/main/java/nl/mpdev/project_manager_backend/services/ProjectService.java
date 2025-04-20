package nl.mpdev.project_manager_backend.services;

import org.springframework.stereotype.Service;
import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.repositories.ProjectRepository;

@Service
public class ProjectService {

  private final ProjectRepository projectRepository;

  public ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }
}
