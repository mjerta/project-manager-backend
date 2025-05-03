package nl.mpdev.project_manager_backend.dto.status.response;

import lombok.Data;

@Data
public class StatusCompleteResponseDto {
  private Long id;
  private String name;
  private String description;
}
