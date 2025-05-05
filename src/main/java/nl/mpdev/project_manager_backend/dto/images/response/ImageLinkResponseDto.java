package nl.mpdev.project_manager_backend.dto.images.response;

import java.net.URI;

import lombok.Data;

@Data
public class ImageLinkResponseDto {
  private URI imageLink;
  private String project;
}
