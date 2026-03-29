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

import nl.mpdev.project_manager_backend.dto.projects.request.ProjectCompleteRequestDto;
import nl.mpdev.project_manager_backend.dto.projects.response.ProjectCompleteResponseDto;
import nl.mpdev.project_manager_backend.mappers.projects.ProjectsMapper;
import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.services.ProjectService;

@RestController
@CrossOrigin(value = "http://localhost:5173")
@RequestMapping("/api/v1")
public class ProjectController {

  private final ProjectService projectService;
  private final ProjectsMapper projectsMapper;

  public ProjectController(ProjectService projectService, ProjectsMapper projectsMapper) {
    this.projectService = projectService;
    this.projectsMapper = projectsMapper;
  }

  @PostMapping("/projects")
  public ResponseEntity<ProjectCompleteResponseDto> addProject(@RequestBody ProjectCompleteRequestDto request) {
    Project addedProject = projectService.addProject(projectsMapper.toEntity(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(projectsMapper.toDto(addedProject));
  }

  @GetMapping("/projects/{id}")
  public ResponseEntity<ProjectCompleteResponseDto> getProjectById(@PathVariable Long id) {
    Project responseProject = projectService.getProjectById(id);
    return ResponseEntity.status(HttpStatus.OK).body(projectsMapper.toDto(responseProject));
  }

  @GetMapping("/projects")
  public ResponseEntity<List<ProjectCompleteResponseDto>> getAllProjects() {
    List<Project> responseAllProjects = projectService.getAllProjects();
    return ResponseEntity.status(HttpStatus.OK).body(responseAllProjects.stream()
        .map(projectsMapper::toDto)
        .toList());
  }

  @PutMapping("/projects/{id}")
  public ResponseEntity<ProjectCompleteResponseDto> updateProject(@PathVariable Long id, @RequestBody ProjectCompleteRequestDto request) {
    Project updatedProject = projectService.updateProject(id, projectsMapper.toEntity(request));
    return ResponseEntity.status(HttpStatus.OK).body(projectsMapper.toDto(updatedProject));
  }

  @DeleteMapping("/projects/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProject(@PathVariable Long id) {
    projectService.deleteProject(id);
  }
}
