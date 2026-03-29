package nl.mpdev.project_manager_backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import nl.mpdev.project_manager_backend.config.TestUiLoginConstants;

@Controller
public class TestUiController {

  @GetMapping("/test-ui")
  public String testUi(Model model) {
    model.addAttribute("tokenStorageKey", TestUiLoginConstants.TEST_UI_STORAGE_KEY);
    return "test-console";
  }

  @GetMapping("/test-ui/login")
  public String initiateLogin(HttpServletRequest request) {
    request.getSession(true).setAttribute(TestUiLoginConstants.TEST_UI_LOGIN_ATTR, true);
    return "redirect:/oauth2/authorization/google";
  }
}
