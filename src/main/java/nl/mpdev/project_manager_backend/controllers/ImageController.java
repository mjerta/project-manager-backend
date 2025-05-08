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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import nl.mpdev.project_manager_backend.dto.images.request.ImageCompleteRequestDto;
import nl.mpdev.project_manager_backend.models.Image;
import nl.mpdev.project_manager_backend.services.ImageService;
import nl.mpdev.project_manager_backend.mappers.image.ImagesMapper;
import nl.mpdev.project_manager_backend.dto.images.response.*;;

@RestController
@CrossOrigin(value = "http://localhost:5173")
@RequestMapping("/api/v1")
public class ImageController {

  private final ImageService imageService;
  private final ImagesMapper imagesMapper;

  public ImageController(ImageService imageService, ImagesMapper imagesMapper) {
    this.imageService = imageService;
    this.imagesMapper = imagesMapper;
  }

  @PostMapping("/images")
  public ResponseEntity<ImageLinkResponseDto> addImage(@Valid @ModelAttribute ImageCompleteRequestDto requestDto)
      throws IOException {
    Image entity = imageService.addImage(imagesMapper.toEntity(requestDto));
    ImageLinkResponseDto responseDto = imagesMapper.toDto(entity);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @GetMapping("/images/{id}")
  public ResponseEntity<ByteArrayResource> getImage(@PathVariable Long id) {
    Image entity = imageService.getImageById(id);
    ImageByteResponseDto dto = imagesMapper.toByteDto(entity);
    return ResponseEntity.ok()
        .header("Id", dto.getId().toString())
        .header(HttpHeaders.CONTENT_TYPE, dto.getContentType())
        .header("Project name", dto.getName())
        .header("Size", dto.getSize().toString())
        .body(new ByteArrayResource(dto.getData()));
  }

  @PutMapping("/images/{id}")
  public ResponseEntity<ImageLinkResponseDto> updateImage(@Valid @ModelAttribute ImageCompleteRequestDto requestDto, @PathVariable Long id)
       throws IOException {
    System.out.println(id);
    Image entity = imageService.updateImage(imagesMapper.toEntity(requestDto), id);
    ImageLinkResponseDto responseDto = imagesMapper.toDto(entity);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @DeleteMapping("/images/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteImage(@PathVariable Long id) {
    imageService.deleteImage(id);
  }
}

