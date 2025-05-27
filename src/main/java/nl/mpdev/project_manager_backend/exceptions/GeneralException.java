package nl.mpdev.project_manager_backend.exceptions;

public class GeneralException extends RuntimeException {
  public GeneralException() {
    super("Something went wrong");
  }
  public GeneralException(String message) {
    super(message);
  }
}

