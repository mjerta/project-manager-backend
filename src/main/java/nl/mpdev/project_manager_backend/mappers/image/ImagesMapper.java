package nl.mpdev.project_manager_backend.mappers.image;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import nl.mpdev.project_manager_backend.dto.images.response.ImageLinkResponseDto;
import nl.mpdev.project_manager_backend.dto.images.response.ImageByteResponseDto;
import nl.mpdev.project_manager_backend.models.Image;

@Component
public class ImagesMapper {

  public ImageLinkResponseDto toDto(Image entity) {
    ImageLinkResponseDto dto = new ImageLinkResponseDto();
    String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/images")
        .path(String.valueOf(entity.getId()))
        .toUriString();
    dto.setImageLink(imageUrl);
    return dto;
  }

  public ImageByteResponseDto toByteDto(Image entity) {
      ImageByteResponseDto dto = new ImageByteResponseDto();
      dto.setId(entity.getId());
      dto.setName(entity.getName());
      dto.setData(entity.getData());
      dto.setProjectId(entity.getProject().getId());
      return dto;
  }
}
