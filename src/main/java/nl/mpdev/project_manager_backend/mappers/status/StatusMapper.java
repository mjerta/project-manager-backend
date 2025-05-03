package nl.mpdev.project_manager_backend.mappers.status;

import org.springframework.stereotype.Component;

import nl.mpdev.project_manager_backend.dto.status.request.StatusCompleteRequestDto;
import nl.mpdev.project_manager_backend.dto.status.response.StatusCompleteResponseDto;
import nl.mpdev.project_manager_backend.models.Status;

@Component
public class StatusMapper {

 public Status toEntity(StatusCompleteRequestDto dto)  {
    Status entity = new Status();
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    return entity;
  }


  public StatusCompleteResponseDto toDto(Status entity) {
    StatusCompleteResponseDto dto = new StatusCompleteResponseDto();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
  }
}
