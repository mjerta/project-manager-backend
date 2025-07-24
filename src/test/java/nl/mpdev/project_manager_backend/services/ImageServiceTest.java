package nl.mpdev.project_manager_backend.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import nl.mpdev.project_manager_backend.models.Image;
import nl.mpdev.project_manager_backend.models.Project;
import nl.mpdev.project_manager_backend.repositories.ImageRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
  @Mock
  private ImageRepository imageRepository;
  @Mock
  private ProjectService projectService;
  @InjectMocks
  private ImageService imageService;

  private Image image1;
  private Image image2;
  private Project project;

  @BeforeEach
  void setUp() throws IOException {
    project = new Project();
    project.setId(1L);

    image1 = new Image();
    image1.setId(1L);
    image1.setName("Image 1");
    image1.setContentType("image/jpg");
    image1.setProject(project);
    byte[] jpegBytes = Files.readAllBytes(Paths.get("src/test/java/resources/monkey.jpg"));
    image1.setData(jpegBytes);
    image1.setSize(jpegBytes.length);

    image2 = new Image();
    image2.setId(2L);
    image2.setName("Image 2");
    image2.setContentType("image/jpg");
    image2.setProject(project);
    byte[] jpegBytes2 = Files.readAllBytes(Paths.get("src/test/java/resources/sheep.jpg"));
    System.out.println(jpegBytes2);
    image2.setData(jpegBytes2);
    image2.setSize(jpegBytes2.length);
  }

  @Test
  void addImage_ShouldReturnSavedImage() {
    // Arrange
    when(imageRepository.save(image1)).thenReturn(image1);
    // Act
    Image saved = imageService.addImage(image1);
    // Assert
    assertEquals(image1, saved);
    verify(imageRepository).save(image1);
  }

  @Test
  void getImageById_ShouldReturnImage_WhenExists() {
    // Arrange
    when(imageRepository.findById(1L)).thenReturn(Optional.of(image1));
    // Act
    Image result = imageService.getImageById(1L);
    // Assert
    assertEquals("Image 1", result.getName());
    verify(imageRepository).findById(1L);
  }

  @Test
  void getImageById_ShouldThrow_WhenNotFound() {
    // Arrange
    when(imageRepository.findById(99L)).thenReturn(Optional.empty());
    // Act and Assert
    RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> imageService.getImageById(99L));
    assertEquals("Image not found", thrown.getMessage());
    verify(imageRepository).findById(99L);
  }

  @Test
  void updateImage_ShouldUpdateAndReturnImage_WhenExists() throws IOException {
    // Arrange
    when(imageRepository.findById(1L)).thenReturn(Optional.of(image1));
    when(projectService.getProjectById(anyLong())).thenReturn(project);
    when(imageRepository.save(any(Image.class))).thenAnswer(i -> i.getArgument(0));
    // Act
    Image updated = imageService.updateImage(image2, 1L);
    // Assert
    assertEquals("Image 2", updated.getName());
    assertEquals("image/jpg", updated.getContentType());
    assertEquals(image2.getSize(), updated.getSize());
    assertEquals(project, updated.getProject());
    verify(imageRepository).findById(1L);
    verify(imageRepository).save(image1);
    verify(projectService).getProjectById(project.getId());
  }

  @Test
  void updateImage_ShouldThrow_WhenNotFound() {
    // Arrange
    when(imageRepository.findById(99L)).thenReturn(Optional.empty());
    // Act
    Image updateData = new Image();
    updateData.setProject(project);
    RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class,
        () -> imageService.updateImage(updateData, 99L));
    // Assert
    assertEquals("Image not found", thrown.getMessage());
    verify(imageRepository).findById(99L);
  }

  @Test
  void deleteImage_ShouldDelete_WhenExists() {
    // Arrange
    when(imageRepository.existsById(1L)).thenReturn(true);
    doNothing().when(imageRepository).deleteById(1L);
    // Act and Assert
    assertDoesNotThrow(() -> imageService.deleteImage(1L));
    verify(imageRepository).existsById(1L);
    verify(imageRepository).deleteById(1L);
  }

  @Test
  void deleteImage_ShouldThrow_WhenNotFound() {
    // Arrange
    when(imageRepository.existsById(99L)).thenReturn(false);
    // Act
    RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> imageService.deleteImage(99L));
    // Assert
    assertEquals("Image not found", thrown.getMessage());
    verify(imageRepository).existsById(99L);
    verify(imageRepository, never()).deleteById(anyLong());
  }
}
