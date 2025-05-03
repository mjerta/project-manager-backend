package nl.mpdev.project_manager_backend.dto.status.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusCompleteRequestDto {
  @Size(min = 3, max = 20, message = "{status.name.size}")
  private String name;
  @Size(max = 100, message = "{status.description.size}")
  private String description;
}
