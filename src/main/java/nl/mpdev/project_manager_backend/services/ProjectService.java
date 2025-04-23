package nl.mpdev.project_manager_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.repositories.ProjectRepository;

@Service
public class ProjectService {

  private final ProjectRepository projectRepository;

  public ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  public Project addProject(Project project) {
    return projectRepository.save(project);
  }

  public Project getProjectById(Long id) {
    return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Something went wrong"));
  }

  public List<Project> getAllProjects() {
    return projectRepository.findAll();
  }

  public Project updateProject(Long id, Project requestUpdateProject) {
    Project existingProject = projectRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Something went wrong"));
    existingProject.setTitle(requestUpdateProject.getTitle());
    existingProject.setStatus(requestUpdateProject.getStatus());
    existingProject.setDescription(requestUpdateProject.getDescription());
    return existingProject;
  } 

  public void deleteProject(Long id){
    projectRepository.deleteById(id);
  }
}
