package nl.mpdev.project_manager_backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import nl.mpdev.project_manager_backend.dto.status.request.StatusCompleteRequestDto;
import nl.mpdev.project_manager_backend.dto.status.response.StatusCompleteResponseDto;
import nl.mpdev.project_manager_backend.mappers.status.StatusMapper;
import nl.mpdev.project_manager_backend.models.Status;
import nl.mpdev.project_manager_backend.services.StatusService;

@RestController
@CrossOrigin(value = "http://localhost:5173")
@RequestMapping("/api/v1")
public class StatusController {

  private final StatusService statusService;
  private final StatusMapper statusMapper;

  public StatusController(StatusService statusService, StatusMapper statusMapper) {
    this.statusService = statusService;
    this.statusMapper = statusMapper;
  }

  @PostMapping("/status")
  public ResponseEntity<StatusCompleteResponseDto> addStatus(@RequestBody @Valid StatusCompleteRequestDto request) {
    Status addedStatus = statusService.addStatus(statusMapper.toEntity(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(statusMapper.toDto(addedStatus));
  }

  @GetMapping("/status/{id}")
  public ResponseEntity<StatusCompleteResponseDto> getStatusById(@PathVariable Long id) {
    Status responseStatus = statusService.getStatusById(id);
    return ResponseEntity.status(HttpStatus.OK).body(statusMapper.toDto(responseStatus));
  }

  @GetMapping("/status")
  public ResponseEntity<List<StatusCompleteResponseDto>> getAllStatus() {
    List<StatusCompleteResponseDto> dtoList = statusService.getAllStatus().stream()
      .map(statusMapper::toDto)
      .toList();
    return ResponseEntity.status(HttpStatus.OK).body(dtoList);
  }

  @PutMapping("/status/{id}")
  public ResponseEntity<StatusCompleteResponseDto> updateStatus(@PathVariable Long id, @RequestBody @Valid StatusCompleteRequestDto request) {
    Status updatedStatus = statusService.updateStatus(id, statusMapper.toEntity(request));
    return ResponseEntity.status(HttpStatus.OK).body(statusMapper.toDto(updatedStatus));
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/status/{id}")
  public void deleteStatus(@PathVariable Long id) {
    statusService.deleteStatus(id);
  }
}
