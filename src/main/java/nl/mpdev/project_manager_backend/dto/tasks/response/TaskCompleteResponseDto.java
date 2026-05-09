package nl.mpdev.project_manager_backend.dto.tasks.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCompleteResponseDto {
  private Long id;
  private String name;
  private String description;
  private Long projectId;
  private String projectTitle;
  private Long statusId;
  private String statusName;
}
