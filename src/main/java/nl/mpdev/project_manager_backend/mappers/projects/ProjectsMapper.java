package nl.mpdev.project_manager_backend.mappers.projects;

import org.springframework.stereotype.Component;
import nl.mpdev.project_manager_backend.models.*;
import nl.mpdev.project_manager_backend.dto.projects.request.ProjectCompleteRequestDto;
import nl.mpdev.project_manager_backend.dto.projects.response.ProjectCompleteResponseDto;
import nl.mpdev.project_manager_backend.services.*;

@Component
public class ProjectsMapper {

  private final StatusService statusService;

  public ProjectsMapper(StatusService statusService) {
    this.statusService = statusService;
  }

  public Project toEntity(ProjectCompleteRequestDto requestDto) {
    Project project = new Project();
    project.setTitle(requestDto.getTitle());
    project.setDescription(requestDto.getDescription());
    project.setStatus(statusService.getStatusById(requestDto.getStatus_id()));
    return project;
  }


  public ProjectCompleteResponseDto toDto(Project entity) {
    ProjectCompleteResponseDto dto = new ProjectCompleteResponseDto();
    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setStatus(entity.getStatus().getName());
    dto.setDescription(entity.getDescription());
    return dto;
  }
}

