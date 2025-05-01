package nl.mpdev.project_manager_backend.controllers;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import nl.mpdev.project_manager_backend.models.Image;
import nl.mpdev.project_manager_backend.services.ImageService;

@RestController
@CrossOrigin(value = "http://localhost:5173")
@RequestMapping("/api/v1")
public class ImageController {

  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @PostMapping("/images")
  public ResponseEntity<String> addImage(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "name") String name,
      @RequestParam(value = "project") Long projectId) throws IOException {
    Image addedImage = imageService.addImage(file, name, projectId);
    String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/images/")
        .path(String.valueOf(addedImage.getId()))
        .toUriString();
    return ResponseEntity.status(HttpStatus.CREATED).body(imageUrl);
  }

  @GetMapping("/images/{id}")
  public ResponseEntity<ByteArrayResource> getImage(@PathVariable Long id) {
    Image image = imageService.getImageById(id);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, image.getContentType())
        .body(new ByteArrayResource(image.getData()));
  }

  @PutMapping("/images/{id}")
  public ResponseEntity<String> updateImage(
      @PathVariable Long id,
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "name") String name) throws IOException {
    Image updatedImage = imageService.updateImage(id, file, name);
    String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/images/")
        .path(String.valueOf(updatedImage.getId()))
        .toUriString();
    return ResponseEntity.status(HttpStatus.OK).body(imageUrl);
  }

  @DeleteMapping("/images/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteImage(@PathVariable Long id) {
    imageService.deleteImage(id);
  }
}
