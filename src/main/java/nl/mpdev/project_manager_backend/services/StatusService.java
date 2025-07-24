package nl.mpdev.project_manager_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import nl.mpdev.project_manager_backend.exceptions.RecordNotFoundException;
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

  public Status getStatusById(Long id) {
    System.out.println(id);
    return statusRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Status not found"));
  }

  public List<Status> getAllStatus() {
    return statusRepository.findAll();
  }

  public Status updateStatus(Long id, Status requestUpdateStatus) {
    Status existingStatus = statusRepository.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Status not found"));
    existingStatus.setName(requestUpdateStatus.getName());
    existingStatus.setDescription(requestUpdateStatus.getDescription());
    return statusRepository.save(existingStatus);
  }

  public void deleteStatus(Long id) {
    if (!statusRepository.existsById(id)) {
      throw new RecordNotFoundException("Status not found");
    }
    statusRepository.deleteById(id);
  }
}
