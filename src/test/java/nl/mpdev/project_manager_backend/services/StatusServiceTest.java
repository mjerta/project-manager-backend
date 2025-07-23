package nl.mpdev.project_manager_backend.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.mpdev.project_manager_backend.exceptions.RecordNotFoundException;
import nl.mpdev.project_manager_backend.models.Status;
import nl.mpdev.project_manager_backend.repositories.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusServiceTest {
  @Mock
  private StatusRepository statusRepository;

  @InjectMocks
  private StatusService statusService;

  private Status status1;
  private Status status2;

  @BeforeEach
  void setup() {
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
  void addStatus_ShouldReturnSavedStatus() {
    // Arrange
    when(statusRepository.save(status1)).thenReturn(status1);

    // Act
    Status saved = statusService.addStatus(status1);

    // Assert
    assertEquals(status1, saved);
    verify(statusRepository).save(status1);
  }

  @Test
  void getStatusById_ShouldReturnStatus_WhenExists() {
    // Arrange
    when(statusRepository.findById(1L)).thenReturn(Optional.of(status1));

    // Act
    Status result = statusService.getStatusById(1L);

    // Assert
    assertEquals("Open", result.getName());
    verify(statusRepository).findById(1L);
  }

  @Test
  void getStatusById_ShouldThrow_WhenNotFound() {
    // Arrange
    when(statusRepository.findById(99L)).thenReturn(Optional.empty());

    // Act and Assert
    RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
      statusService.getStatusById(99L);
    });
    assertEquals("Status not found", thrown.getMessage());
    verify(statusRepository).findById(99L);
  }

  @Test
  void getAllStatus_ShouldReturnListOfStatuses() {
    // Arrange
    List<Status> statuses = Arrays.asList(status1, status2);
    when(statusRepository.findAll()).thenReturn(statuses);

    // Act
    List<Status> result = statusService.getAllStatus();

    // Assert
    assertEquals(2, result.size());
    assertTrue(result.contains(status1));
    assertTrue(result.contains(status2));
    verify(statusRepository).findAll();
  }

  @Test
  void updateStatus_ShouldUpdateAndReturnStatus_WhenExists() {
    // Arrange
    when(statusRepository.findById(1L)).thenReturn(Optional.of(status1));
    when(statusRepository.save(any(Status.class))).thenAnswer(i -> i.getArgument(0));

    Status updateData = new Status();
    updateData.setName("Updated Name");
    updateData.setDescription("Updated description");

    // Act
    Status updated = statusService.updateStatus(1L, updateData);

    // Assert
    assertEquals("Updated Name", updated.getName());
    assertEquals("Updated description", updated.getDescription());
    verify(statusRepository).findById(1L);
    verify(statusRepository).save(status1);
  }

  @Test
  void updateStatus_ShouldThrow_WhenNotFound() {
    // Arrange
    when(statusRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
      statusService.updateStatus(99L, status1);
    });

    assertEquals("Status not found", thrown.getMessage());
    verify(statusRepository).findById(99L);
    verify(statusRepository, never()).save(any());
  }

  @Test
  void deleteStatus_ShouldDelete_WhenExists() {
    // Arrange
    when(statusRepository.existsById(1L)).thenReturn(true);
    doNothing().when(statusRepository).deleteById(1L);

    // Act & Assert
    assertDoesNotThrow(() -> statusService.deleteStatus(1L));
    verify(statusRepository).existsById(1L);
    verify(statusRepository).deleteById(1L);
  }

  @Test
  void deleteStatus_ShouldThrow_WhenNotFound() {
    // Arrange
    when(statusRepository.existsById(99L)).thenReturn(false);

    // Act & Assert
    RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
      statusService.deleteStatus(99L);
    });

    assertEquals("Status not found", thrown.getMessage());
    verify(statusRepository).existsById(99L);
    verify(statusRepository, never()).deleteById(anyLong());
  }
}
