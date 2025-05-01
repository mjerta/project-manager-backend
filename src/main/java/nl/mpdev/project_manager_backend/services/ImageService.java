package nl.mpdev.project_manager_backend.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

  public Image addImage(MultipartFile file, String name, Long projectId) throws IOException {
     if (file.isEmpty()) {
      throw new IllegalArgumentException("Cannot save empty file");
    }
    Image image = new Image();;
    image.setName(name);
    image.setContentType(file.getContentType());
    image.setSize(file.getSize());
    image.setData(file.getBytes());
    if (projectId != null) image.setProject(projectService.getProjectById(projectId));
    return imageRepository.save(image);
  }

  public Image getImageById(Long id){
    return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Something went wrong"));
  }

  public Image updateImage(Long id, MultipartFile file, String name, Long projectId) throws IOException {
    if(file.isEmpty()) {
      throw new IllegalArgumentException("Cannot save empty file");
    }
    Image existingImage = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Something went wrong"));
    existingImage.setName(name);
    existingImage.setContentType(file.getContentType());
    existingImage.setSize(file.getSize());
    existingImage.setData(file.getBytes());
    if (projectId != null) existingImage.setProject(projectService.getProjectById(projectId));
    return imageRepository.save(existingImage);
  }

  public void deleteImage(Long id) {
    if(!imageRepository.existsById(id)) {
      throw new RuntimeException("This record does not exist");
    }
    imageRepository.deleteById(id);
  }
}
