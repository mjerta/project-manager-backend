package nl.mpdev.project_manager_backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @GetMapping("/")
  public String greet() {
    return "Welcome to mpdev!";
  }

}
