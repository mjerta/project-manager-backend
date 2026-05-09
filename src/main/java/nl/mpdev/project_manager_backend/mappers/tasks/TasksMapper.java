package nl.mpdev.project_manager_backend.mappers.tasks;

import java.io.ObjectInputFilter.Status;

import org.springframework.stereotype.Component;

import nl.mpdev.project_manager_backend.dto.tasks.request.TaskCompleteRequestDto;
import nl.mpdev.project_manager_backend.dto.tasks.response.TaskCompleteResponseDto;
import nl.mpdev.project_manager_backend.mappers.status.StatusMapper;
import nl.mpdev.project_manager_backend.models.Task;
import nl.mpdev.project_manager_backend.services.ProjectService;
import nl.mpdev.project_manager_backend.services.StatusService;

@Component
public class TasksMapper {

  private final ProjectService projectService;
  private final StatusService statusService;

  public TasksMapper(ProjectService projectService, StatusService statusService) {
    this.projectService = projectService;
    this.statusService = statusService;
  }

  public Task toEntity(TaskCompleteRequestDto dto) {
    Task task = new Task();
    task.setId(dto.getId());
    task.setName(dto.getName());
    task.setProject(projectService.getProjectById(dto.getProjectId()));
    task.setStatus(statusService.getStatusById(dto.getStatusId()));
    return task;
  }

  public TaskCompleteResponseDto toDto(Task task) {
    TaskCompleteResponseDto dto = new TaskCompleteResponseDto();
    dto.setId(task.getId());
    dto.setName(task.getName());
    dto.setDescription(task.getDescription());
    if (task.getProject() != null) {
      dto.setProjectId(task.getProject().getId());
      dto.setProjectTitle(task.getProject().getTitle());
    }
    if (task.getStatus() != null) {
      dto.setStatusId(task.getStatus().getId());
      dto.setStatusName(task.getStatus().getName());
    }
    return dto;
  }

}
