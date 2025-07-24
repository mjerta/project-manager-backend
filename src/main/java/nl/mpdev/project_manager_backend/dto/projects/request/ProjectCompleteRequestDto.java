package nl.mpdev.project_manager_backend.dto.projects.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCompleteRequestDto {
  @NotBlank
  @Size(min = 3, max = 20, message = "{project.title.size}")
  private String title;
  private String description;
  @NotNull(message = "{project.statusId.notNull}")
  private Long statusId;
}
