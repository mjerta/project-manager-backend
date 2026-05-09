package nl.mpdev.project_manager_backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import nl.mpdev.project_manager_backend.dto.tasks.request.TaskCompleteRequestDto;
import nl.mpdev.project_manager_backend.dto.tasks.response.TaskCompleteResponseDto;
import nl.mpdev.project_manager_backend.mappers.tasks.TasksMapper;
import nl.mpdev.project_manager_backend.models.Task;
import nl.mpdev.project_manager_backend.services.TaskService;

@Validated
@RestController
@RequestMapping("/api/v1")
public class TaskController {

  private final TaskService taskService;
  private final TasksMapper tasksMapper;

  public TaskController(TaskService taskService, TasksMapper tasksMapper) {
    this.taskService = taskService;
    this.tasksMapper = tasksMapper;
  }

  @PostMapping("/tasks")
  public ResponseEntity<TaskCompleteResponseDto> createTask(@Valid @RequestBody TaskCompleteRequestDto request) {
    // TODO: fix mapper and the way i like it in here and fix the repository as well

    Task created = taskService.createTask(tasksMapper.toDto(request));

    return ResponseEntity.status(HttpStatus.CREATED).body(tasksMapper.toDto(created));
  }

  @GetMapping("/tasks/{id}")
  public ResponseEntity<TaskCompleteResponseDto> getTask(@PathVariable Long id) {
    Task task = taskService.getTaskById(id);
    return ResponseEntity.ok(tasksMapper.toDto(task));
  }

  @GetMapping("/tasks")
  public ResponseEntity<List<TaskCompleteResponseDto>> getAllTasks() {
    List<TaskCompleteResponseDto> tasks = taskService.getAllTasks().stream()
        .map(tasksMapper::toDto)
        .toList();
    return ResponseEntity.ok(tasks);
  }

  @PutMapping("/tasks/{id}")
  public ResponseEntity<TaskCompleteResponseDto> updateTask(@PathVariable Long id,
      @Valid @RequestBody TaskCompleteRequestDto request) {
    Task updated = taskService.updateTask(id, request);
    return ResponseEntity.ok(tasksMapper.toDto(updated));
  }

  @DeleteMapping("/tasks/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
  }
}
