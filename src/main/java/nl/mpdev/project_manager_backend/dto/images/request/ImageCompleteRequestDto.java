package nl.mpdev.project_manager_backend.dto.images.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageCompleteRequestDto {
  @NotBlank
  @Size(min = 3, max = 20, message = "{image.name.size}")
  private String name;
  // @NotNull(message = "{image.mutltipart.notnull}")
  private MultipartFile image;
  // @NotNull(message = "{image.projectId.notNull}")
  private Long projectId;
}
