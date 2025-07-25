package nl.mpdev.project_manager_backend.services;

import java.io.IOException;

import org.springframework.stereotype.Service;

import nl.mpdev.project_manager_backend.exceptions.RecordNotFoundException;
import nl.mpdev.project_manager_backend.models.Image;
import nl.mpdev.project_manager_backend.repositories.ImageRepository;

@Service
public class ImageService {

  private final ImageRepository imageRepository;
  private final ProjectService projectService;

  public ImageService(ImageRepository imageRepository, ProjectService projectService) {
    this.imageRepository = imageRepository;
    this.projectService = projectService;
  }

  public Image addImage(Image image) {
    return imageRepository.save(image);
  }

  public Image getImageById(Long id) {
    return imageRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Image not found"));
  }

  public Image updateImage(Image image, Long id) throws IOException {
    Image existingImage = imageRepository.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Image not found"));
    existingImage.setName(image.getName());
    existingImage.setContentType(image.getContentType());
    existingImage.setSize(image.getSize());
    existingImage.setData(image.getData());
    existingImage.setProject(projectService.getProjectById(image.getProject().getId()));
    return imageRepository.save(existingImage);
  }

  public void deleteImage(Long id) {
    if (!imageRepository.existsById(id)) {
      throw new RecordNotFoundException("Image not found");
    }
    imageRepository.deleteById(id);
  }
}
