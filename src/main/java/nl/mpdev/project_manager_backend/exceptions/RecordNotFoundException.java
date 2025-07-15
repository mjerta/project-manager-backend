package nl.mpdev.project_manager_backend.exceptions;

public class RecordNotFoundException extends RuntimeException{
  public RecordNotFoundException() {
    super("Record not found");
  }

  public RecordNotFoundException(String message) {
    super(message);
  }
}

