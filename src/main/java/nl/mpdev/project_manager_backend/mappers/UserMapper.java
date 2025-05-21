package nl.mpdev.project_manager_backend.mappers;

import nl.mpdev.project_manager_backend.dtos.user.UserInputOrUpdateDto;
import nl.mpdev.project_manager_backend.dtos.user.UserOutputDto;
import nl.mpdev.project_manager_backend.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User ToEntity(UserInputOrUpdateDto userInputDto) {
        User user = new User();
        user.setUsername(userInputDto.getUsername());
        user.setPassword(userInputDto.getPassword());
        user.setEmail(userInputDto.getEmail());
        user.setFirstName(userInputDto.getFirstName());
        user.setMiddleName(userInputDto.getMiddleName());
        user.setLastName(userInputDto.getLastName());
        user.setRole(userInputDto.getRole());
        user.setIsActive(userInputDto.getIsActive());

        return user;
    }

    public UserOutputDto ToDto(User user) {
        UserOutputDto userOutputDto = new UserOutputDto();
        userOutputDto.setId(user.getId());
        userOutputDto.setUsername(user.getUsername());
        userOutputDto.setPassword(user.getPassword());
        userOutputDto.setEmail(user.getEmail());
        userOutputDto.setFirstName(user.getFirstName());
        userOutputDto.setMiddleName(user.getMiddleName());
        userOutputDto.setLastName(user.getLastName());
        userOutputDto.setRole(user.getRole());
        userOutputDto.setIsActive(user.getIsActive());
        userOutputDto.setCreatedAt(user.getCreatedAt());
        userOutputDto.setUpdatedAt(user.getUpdatedAt());

        return userOutputDto;
    }
}
