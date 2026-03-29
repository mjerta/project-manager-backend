package nl.mpdev.project_manager_backend.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class TestController {

  @GetMapping("/first-test")
  Map<String, String> firstTest() {
    return Map.of("message", "firstTest");
  }

}
