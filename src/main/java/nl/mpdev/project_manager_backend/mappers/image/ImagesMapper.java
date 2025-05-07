package nl.mpdev.project_manager_backend.mappers.image;

import java.io.IOException;
import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import nl.mpdev.project_manager_backend.dto.images.response.ImageLinkResponseDto;
import nl.mpdev.project_manager_backend.dto.images.request.ImageCompleteRequestDto;
import nl.mpdev.project_manager_backend.dto.images.response.ImageByteResponseDto;
import nl.mpdev.project_manager_backend.models.Image;
import nl.mpdev.project_manager_backend.services.ProjectService;

@Component
public class ImagesMapper {

  private final ProjectService projectsService;

  public ImagesMapper(ProjectService projectsService) {
    this.projectsService = projectsService;
  }

  public Image toEntity(ImageCompleteRequestDto dto) throws IOException {
    Image entity = new Image();
    entity.setName(dto.getName());
    if (dto.getImage() != null) {
      entity.setData(dto.getImage().getBytes());
      entity.setSize(dto.getImage().getSize());
      entity.setContentType(dto.getImage().getContentType());
    }
    if (dto.getProjectId() != null) {
      entity.setProject(projectsService.getProjectById(dto.getProjectId()));
    }
    return entity;
  }

  public ImageLinkResponseDto toDto(Image entity) {
    ImageLinkResponseDto dto = new ImageLinkResponseDto();
    URI imageUrl = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/images/")
        .path(String.valueOf(entity.getId()))
        .toUriString());
    dto.setImageLink(imageUrl);
    dto.setProject(entity.getName());
    return dto;
  }

  public ImageByteResponseDto toByteDto(Image entity) {
    ImageByteResponseDto dto = new ImageByteResponseDto();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setSize(entity.getSize());
    dto.setData(entity.getData());
    dto.setProject(entity.getProject().getTitle());
    dto.setContentType(entity.getContentType());
    return dto;
  }
}
