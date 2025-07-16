package nl.mpdev.project_manager_backend.services;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.models.Status;
import nl.mpdev.project_manager_backend.repositories.*;
import nl.mpdev.project_manager_backend.repositories.ProjectRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
public class StatusServiceTest {
  @Mock
  private StatusRepository statusRepository;

  @InjectMocks
  private StatusService statusService;

  private Status status1;
  private Status status2;

  @BeforeEach
  void setup() {
('To Do', 'Task has not been started yet'),
    status1 = new Status();
    status1.setId(1L);
    status1.setName("Open");
    status1.setDescription("Project is open");

    status2 = new Status();
    status2.setId(2L);
    status2.setName("Closed");
    status2.setDescription("Project is closed");
  }

  @Test
  void getStatusById_ShouldReturnStatus_WhenExists() {
    // Arrange
    when(statusRepository.findById(1L)).thenReturn(Optional.of(status1));

    // Act
    Status result = statusService.getStatusById(1L);

    // Assert
    assertEquals('To Do', actual);
    verify(statusRepository).findById(1L);
  }

}
