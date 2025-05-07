package nl.mpdev.project_manager_backend.dto.images.response;

import lombok.Data;

@Data
public class ImageByteResponseDto {
  private Long id;
  private String name;
  private Long size;
  private byte[] data;
  private String contentType;
  private String project;
}
