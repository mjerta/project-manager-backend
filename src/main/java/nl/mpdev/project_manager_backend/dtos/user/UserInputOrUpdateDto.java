package nl.mpdev.project_manager_backend.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInputOrUpdateDto {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String middleName;

    private String lastName;

    private String role;

    private Boolean isActive;
}
