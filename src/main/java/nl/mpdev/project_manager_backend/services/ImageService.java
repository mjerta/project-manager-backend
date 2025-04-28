package nl.mpdev.project_manager_backend.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import nl.mpdev.project_manager_backend.models.Image;
import nl.mpdev.project_manager_backend.repositories.ImageRepository;

@Service
public class ImageService {

  private final ImageRepository imageRepository;

  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  public Image addImage(MultipartFile file, String name) throws IOException {
     if (file.isEmpty()) {
      throw new IllegalArgumentException("Cannot save empty file");
    }
    Image image = new Image();;
    image.setName(name);
    image.setContentType(file.getContentType());
    image.setSize(file.getSize());
    image.setData(file.getBytes());
    return imageRepository.save(image);
  }

  public Image getImageById(Long id){
    return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Something went wrong"));
  }

  public Image updateImage(Long id, MultipartFile file, String name) throws IOException {
    if(file.isEmpty()) {
      throw new IllegalArgumentException("Cannot save empty file");
    }
    Image existingImage = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Something went wrong"));
    existingImage.setName(name);
    existingImage.setContentType(file.getContentType());
    existingImage.setSize(file.getSize());
    existingImage.setData(file.getBytes());
    return imageRepository.save(existingImage);
  }

  public void deleteImage(Long id) {
    if(!imageRepository.existsById(id)) {
      throw new RuntimeException("This record does not exist");
    }
    imageRepository.deleteById(id);
  }
}
