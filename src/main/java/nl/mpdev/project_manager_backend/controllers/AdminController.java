package nl.mpdev.project_manager_backend.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value = "http://localhost:5173")
@RequestMapping("/api/v1")
public class AdminController {

  @GetMapping("/admin")
  Map<String, String> getTest() {
    return Map.of("message", "admin message");
  }
}
