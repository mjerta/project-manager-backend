package nl.mpdev.project_manager_backend.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
  public ResponseEntity<Long> addImage(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "name") String name) throws IOException {
    System.out.println(name);
    Image addedImage = imageService.addImage(file, name);
    return ResponseEntity.status(HttpStatus.CREATED).body(addedImage.getId());
  }
}
