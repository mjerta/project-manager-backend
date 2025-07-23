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
import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.models.Status;
import nl.mpdev.project_manager_backend.repositories.ProjectRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
  @Mock
  private ProjectRepository projectRepository;

  @InjectMocks
  private ProjectService projectService;

  private Project project1;
  private Project project2;
  private Status status;

  @BeforeEach
  void setUp() {
    status = new Status();
    status.setId(1L);
    status.setName("Open");
    status.setDescription("A project status");

    project1 = new Project();
    project1.setId(1L);
    project1.setTitle("Project 1");
    project1.setDescription("Description 1");
    project1.setStatus(status);

    project2 = new Project();
    project2.setId(2L);
    project2.setTitle("Project 2");
    project2.setDescription("Description 2");
    project2.setStatus(status);
  }

  @Test
  void addProject_ShouldReturnSavedProject() {
    // Arrange
    when(projectRepository.save(project1)).thenReturn(project1);

    // Act
    Project saved = projectService.addProject(project1);

    // Assert
    assertEquals(project1, saved);
    verify(projectRepository).save(project1);
  }

  @Test
  void getProjectById_ShouldReturnProject_WhenExists() {
    // Arrange
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project1));

    // Act
    Project result = projectService.getProjectById(1L);

    // Assert
    assertEquals("Project 1", result.getTitle());
    verify(projectRepository).findById(1L);
  }

  @Test
  void getProjectById_ShouldThrow_WhenNotFound() {
    // Arrange
    when(projectRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class,
        () -> projectService.getProjectById(99L));
    assertEquals("Project not found",thrown.getMessage());
    verify(projectRepository).findById(99L);
  }

  @Test
  void getAllProjects_ShouldReturnListOfProjects() {
    // Arrange
    List<Project> projects = Arrays.asList(project1, project2);
    when(projectRepository.findAll()).thenReturn(projects);

    // Act
    List<Project> result = projectService.getAllProjects();

    // Assert
    assertEquals(2, result.size());
    assertTrue(result.contains(project1));
    assertTrue(result.contains(project2));
    verify(projectRepository).findAll();
  }

  @Test
  void updateProject_ShouldUpdateAndReturnProject_WhenExists() {
    // Arrange
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project1));

    Project updateData = new Project();
    updateData.setTitle("Updated Title");
    updateData.setDescription("Updated Description");
    updateData.setStatus(status);

    // Act
    Project updated = projectService.updateProject(1L, updateData);

    // Assert
    assertEquals("Updated Title", updated.getTitle());
    assertEquals("Updated Description", updated.getDescription());
    assertEquals(status, updated.getStatus());
    verify(projectRepository).findById(1L);
  }

  @Test
  void updateProject_ShouldThrow_WhenNotFound() {
    // Arrange
    when(projectRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> projectService.updateProject(99L, project1));

    assertEquals("Project not found", thrown.getMessage());
    verify(projectRepository).findById(99L);
    verify(projectRepository, never()).save(any());
  }

  @Test
  void deleteProject_ShouldDelete_WhenExists() {
    // Arrange
    when(projectRepository.existsById(1L)).thenReturn(true);
    doNothing().when(projectRepository).deleteById(1L);

    // Act & Assert
    assertDoesNotThrow(() -> projectService.deleteProject(1L));
    verify(projectRepository).existsById(1L);
    verify(projectRepository).deleteById(1L);
  }

  @Test
  void deleteProject_ShouldThrow_WhenNotFound() {
    // Arrange
    when(projectRepository.existsById(99L)).thenReturn(false);

    // Act & Assert
    RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> projectService.deleteProject(99L));
    assertEquals("Project not found", thrown.getMessage());
    verify(projectRepository).existsById(99L);
    verify(projectRepository, never()).deleteById(anyLong());
  }
}
