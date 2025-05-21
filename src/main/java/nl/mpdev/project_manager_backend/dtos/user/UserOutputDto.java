package nl.mpdev.project_manager_backend.dtos.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserOutputDto {

    private UUID id;

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String middleName;

    private String lastName;

    private String role;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
