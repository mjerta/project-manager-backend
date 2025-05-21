package nl.mpdev.project_manager_backend.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "users")
public class User extends BaseObject {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String middleName;

    private String lastName;

    private String role;

    private Boolean isActive;

    public User() {
        super();
    }
}
