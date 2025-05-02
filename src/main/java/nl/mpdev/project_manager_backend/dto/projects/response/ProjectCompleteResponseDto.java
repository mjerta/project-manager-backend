package nl.mpdev.project_manager_backend.dto.projects.response;

import java.util.List;
import lombok.Data;

@Data
public class ProjectCompleteResponseDto {
  private Long id;
  private String title;
  private String description;
  private String status;
  private List<String> imageLinks;
}
