package nl.mpdev.project_manager_backend.mappers.tasks;

import org.springframework.stereotype.Component;

import nl.mpdev.project_manager_backend.dto.tasks.response.TaskCompleteResponseDto;
import nl.mpdev.project_manager_backend.models.Task;

@Component
public class TasksMapper {

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
