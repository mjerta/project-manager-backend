package nl.mpdev.project_manager_backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.services.ProjectService;

@RestController
@CrossOrigin(value = "http://localhost:5173")
@RequestMapping("/api/v1")
public class ProjectController {

  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @PostMapping("/projects")
  public ResponseEntity<Project> addProject(@RequestBody Project project) {
    System.out.println(project.getTitle());
    Project addedProject = projectService.addProject(project);
    return ResponseEntity.status(HttpStatus.CREATED).body(addedProject);
  }

  @GetMapping("/projects/{id}")
  public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
    Project responseProject = projectService.getProjectById(id);
    return ResponseEntity.status(HttpStatus.OK).body(responseProject);
  }

  @GetMapping("/projects")
  public ResponseEntity<List<Project>> getAllProjects() {
    List<Project> responseAllProjects = projectService.getAllProjects();
    return ResponseEntity.status(HttpStatus.OK).body(responseAllProjects);
  }

  @PutMapping("/projects/{id}")
  public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project){
    Project updatedProject = projectService.updateProject(id, project);
    return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
  }

  @DeleteMapping("/projects/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProject(@PathVariable Long id){
    projectService.deleteProject(id);
  }
}
