package nl.mpdev.project_manager_backend.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.mpdev.project_manager_backend.exceptions.RecordNotFoundException;
import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.models.Status;
import nl.mpdev.project_manager_backend.models.Task;
import nl.mpdev.project_manager_backend.repositories.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskService taskService;

  private Project project;
  private Status status;

  @BeforeEach
  void setUp() {
    project = new Project();
    project.setId(10L);
    project.setTitle("Project Alpha");

    status = new Status();
    status.setId(5L);
    status.setName("OPEN");
  }

  @Test
  void createTask_ShouldPersistTask() {
    Task request = buildTask();
    when(taskRepository.save(request)).thenAnswer(invocation -> {
      Task saved = invocation.getArgument(0);
      saved.setId(1L);
      return saved;
    });

    Task result = taskService.createTask(request);

    verify(taskRepository).save(request);
    assertEquals(1L, result.getId());
    assertEquals("New task", result.getName());
    assertSame(project, result.getProject());
    assertSame(status, result.getStatus());
  }

  @Test
  void getTaskById_ShouldReturnTask_WhenPresent() {
    Task entity = new Task();
    entity.setId(2L);
    when(taskRepository.findById(2L)).thenReturn(Optional.of(entity));

    Task found = taskService.getTaskById(2L);

    assertSame(entity, found);
    verify(taskRepository).findById(2L);
  }

  @Test
  void getTaskById_ShouldThrow_WhenMissing() {
    when(taskRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(RecordNotFoundException.class, () -> taskService.getTaskById(99L));
    verify(taskRepository).findById(99L);
  }

  @Test
  void getAllTasks_ShouldReturnRepositoryResult() {
    List<Task> tasks = List.of(new Task(), new Task());
    when(taskRepository.findAll()).thenReturn(tasks);

    List<Task> result = taskService.getAllTasks();

    assertSame(tasks, result);
    verify(taskRepository).findAll();
  }

  @Test
  void updateTask_ShouldMutateExistingTask() {
    Task existing = new Task();
    existing.setId(7L);
    existing.setName("Old");
    existing.setDescription("Old description");

    Task updateRequest = buildTask();
    Project newProject = new Project();
    newProject.setId(20L);
    newProject.setTitle("Project Beta");
    Status newStatus = new Status();
    newStatus.setId(8L);
    newStatus.setName("DONE");
    updateRequest.setProject(newProject);
    updateRequest.setStatus(newStatus);

    when(taskRepository.findById(7L)).thenReturn(Optional.of(existing));

    Task updated = taskService.updateTask(7L, updateRequest);

    assertSame(existing, updated);
    assertEquals(updateRequest.getName(), updated.getName());
    assertEquals(updateRequest.getDescription(), updated.getDescription());
    assertSame(newProject, updated.getProject());
    assertSame(newStatus, updated.getStatus());
  }

  @Test
  void deleteTask_ShouldRemoveEntity_WhenExists() {
    when(taskRepository.existsById(3L)).thenReturn(true);
    doNothing().when(taskRepository).deleteById(3L);

    assertDoesNotThrow(() -> taskService.deleteTask(3L));
    verify(taskRepository).existsById(3L);
    verify(taskRepository).deleteById(3L);
  }

  @Test
  void deleteTask_ShouldThrow_WhenMissing() {
    when(taskRepository.existsById(anyLong())).thenReturn(false);

    assertThrows(RecordNotFoundException.class, () -> taskService.deleteTask(11L));
    verify(taskRepository).existsById(11L);
    verifyNoMoreInteractions(taskRepository);
  }

  private Task buildTask() {
    Task task = new Task();
    task.setName("New task");
    task.setDescription("Complete this");
    task.setProject(project);
    task.setStatus(status);
    return task;
  }
}
