package nl.mpdev.project_manager_backend.exceptions;

public class DuplicateRecordFound extends RuntimeException {
  public DuplicateRecordFound() {
    super("Duplicate record is found");
  }

  public DuplicateRecordFound(String message) {
    super(message);
  }
}

