package nl.mpdev.project_manager_backend.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
// For later use
// import io.jsonwebtoken.security.SignatureException;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionsHandler {

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream().
      collect(Collectors.toMap(
        FieldError::getField,
        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Invalid value")
      ));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(value = DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleException(DataIntegrityViolationException ex) {
    Map<String, String> error = new LinkedHashMap<>();
    error.put("error", "Constrain error in database");
    // extra details about error
    error.put("error-message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(value = IndexOutOfBoundsException.class)
  public ResponseEntity<Object> handleException(IndexOutOfBoundsException ex) {
    Map<String, String> error = new LinkedHashMap<>();
    error.put("error", "Index out of bounds");
    // extra details about error
    error.put("error-message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(value = HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleException(HttpMessageNotReadableException ex) {
    Map<String, String> error = new LinkedHashMap<>();
    error.put("error", "Invalid properties inside your request");
    // extra details about error
    error.put("error-message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // @ExceptionHandler(value = BadCredentialsException.class)
  // public ResponseEntity<Object> handleException(BadCredentialsException ex) {
  //   Map<String, String> error = new LinkedHashMap<>();
  //   error.put("error", "Bad credentials");
  //   // check if this status is correct
  //   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  // }

  // @ExceptionHandler(value = SignatureException.class)
  // public ResponseEntity<Object> handleException(SignatureException ex) {
  //   Map<String, String> error = new LinkedHashMap<>();
  //   error.put("error", "Not authorized for this endpoint");
  //   // extra details about error
  //   error.put("error-message", ex.getMessage());
  //   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  // }

  // @ExceptionHandler(value =  UsernameNotFoundException.class)
  // public ResponseEntity<Object> handleException(UsernameNotFoundException ex) {
  //   Map<String, String> error = new LinkedHashMap<>();
  //   error.put("error", "Not authorized for this endpoint");
  //   // extra details about error
  //   error.put("error-message", ex.getMessage());
  //   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  // }

  //custom exceptions

  @ExceptionHandler(RecordNotFoundException.class)
  public ResponseEntity<Object> handleException(RecordNotFoundException ex) {
    Map<String, String> error = new LinkedHashMap<>();
    error.put("error-message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(GeneralException.class)
  public ResponseEntity<Object> handleException(GeneralException ex) {
    Map<String, String> error = new LinkedHashMap<>();
    error.put("error-message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(DuplicateRecordFound.class)
  public ResponseEntity<Object> handleException(DuplicateRecordFound ex) {
    Map<String, String> error = new LinkedHashMap<>();
    error.put("error-message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}

