package nl.mpdev.project_manager_backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value = "http://localhost:5173")
@RequestMapping("/api/v1")
public class AdminController {

  @GetMapping("/admin")
  String getTest() {
    return "This is for admins";
  }
}
