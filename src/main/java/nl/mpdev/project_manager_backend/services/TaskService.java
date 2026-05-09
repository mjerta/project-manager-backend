package nl.mpdev.project_manager_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.mpdev.project_manager_backend.dto.tasks.request.TaskCompleteRequestDto;
import nl.mpdev.project_manager_backend.exceptions.RecordNotFoundException;
import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.models.Status;
import nl.mpdev.project_manager_backend.models.Task;
import nl.mpdev.project_manager_backend.repositories.TaskRepository;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final ProjectService projectService;
  private final StatusService statusService;

  public TaskService(TaskRepository taskRepository, ProjectService projectService, StatusService statusService) {
    this.taskRepository = taskRepository;
    this.projectService = projectService;
    this.statusService = statusService;
  }

  @Transactional
  public Task createTask(Task request) {
    return taskRepository.save(request);
  }

  public Task getTaskById(Long id) {
    return taskRepository.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Task not found"));
  }

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  @Transactional
  public Task updateTask(Long id, Task request) {
    Task existingTask = getTaskById(id);
    applyRequest(request);
    return existingTask;
  }

  public void deleteTask(Long id) {
    if (!taskRepository.existsById(id)) {
      throw new RecordNotFoundException("Task not found");
    }
    taskRepository.deleteById(id);
  }

  private void applyRequest(Task request) {
    request.setName(request.getName());
    request.setDescription(request.getDescription());
    request.setProject(request.getProject());
    request.setStatus(request.getStatus());
  }
}
