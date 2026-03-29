package nl.mpdev.project_manager_backend.dto.tasks.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCompleteRequestDto {

  @NotBlank
  @Size(min = 3, max = 60)
  private String name;

  @Size(max = 255)
  private String description;

  @NotNull
  private Long projectId;

  @NotNull
  private Long statusId;
}
