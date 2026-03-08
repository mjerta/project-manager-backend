package nl.mpdev.project_manager_backend.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import nl.mpdev.project_manager_backend.config.TestUiLoginConstants;

@Controller
public class TestUiController {

  private String adminEmail;

  @GetMapping("/test-ui")
  public String testUi(Model model) {
    model.addAttribute("tokenStorageKey", TestUiLoginConstants.TEST_UI_STORAGE_KEY);
    model.addAttribute("adminEndpoint", "/api/v1/admin");
    model.addAttribute("statusEndpoint", "/api/v1/status");
    model.addAttribute("firstTestEndpoint", "/api/v1/first-test");
    model.addAttribute("adminEmail", adminEmail);
    return "test-console";
  }

  @GetMapping("/test-ui/login")
  public String initiateLogin(HttpServletRequest request) {
    request.getSession(true).setAttribute(TestUiLoginConstants.TEST_UI_LOGIN_ATTR, true);
    return "redirect:/oauth2/authorization/google";
  }
}
