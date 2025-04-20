package nl.mpdev.project_manager_backend.services;

import org.springframework.stereotype.Service;
import nl.mpdev.project_manager_backend.models.Status;
import nl.mpdev.project_manager_backend.repositories.StatusRepository;

@Service
public class StatusService {

  private final StatusRepository statusRepository;

  public StatusService(StatusRepository statusRepository) {
    this.statusRepository = statusRepository;
  }

  public Status addStatus(Status status) {
    return statusRepository.save(status);
  }

  public Status getStatusById(Long id){
    System.out.println(id);
    return statusRepository.findById(id).orElseThrow(() -> new RuntimeException("Something went wrong here"));
  }
}
