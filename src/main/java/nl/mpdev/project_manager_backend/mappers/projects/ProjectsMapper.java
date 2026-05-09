package nl.mpdev.project_manager_backend.mappers.projects;

import org.springframework.stereotype.Component;

import nl.mpdev.project_manager_backend.dto.projects.request.ProjectCompleteRequestDto;
import nl.mpdev.project_manager_backend.dto.projects.response.ProjectCompleteResponseDto;
import nl.mpdev.project_manager_backend.mappers.image.ImagesMapper;
import nl.mpdev.project_manager_backend.models.Project;

@Component
public class ProjectsMapper {

  private final ImagesMapper imagesMapper;

  public ProjectsMapper(ImagesMapper imagesMapper) {
    this.imagesMapper = imagesMapper;
  }

  public Project toEntity(ProjectCompleteRequestDto requestDto) {
    Project project = new Project();
    project.setTitle(requestDto.getTitle());
    project.setDescription(requestDto.getDescription());
    return project;
  }

  public ProjectCompleteResponseDto toDto(Project entity) {
    ProjectCompleteResponseDto dto = new ProjectCompleteResponseDto();
    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setDescription(entity.getDescription());
    dto.setImageLinks(
      entity.getImages().stream()
        .map(image -> imagesMapper.toDto(image).getImageLink())
        .toList()
    );
    return dto;
  }
}
