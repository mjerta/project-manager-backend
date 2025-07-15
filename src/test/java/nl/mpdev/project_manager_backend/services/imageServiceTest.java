package nl.mpdev.project_manager_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.models.Status;
import nl.mpdev.project_manager_backend.repositories.ProjectRepository;

public class imageServiceTest {
  @Mock
  private ProjectRepository projectRepository;

  @Mock
  private StatusService statusService;

  @InjectMocks
  private ProjectService projectService;
  private Project sampleProject;
  private Project updatedProject;

  @BeforeEach
  void setup() {
    sampleProject = new Project();
    sampleProject.setId(1l);
    sampleProject.setTitle("Initial title");
    sampleProject.setStatus(statusService.getStatusById(1l));
    sampleProject.setDescription("Simple project");
  }
  
}
