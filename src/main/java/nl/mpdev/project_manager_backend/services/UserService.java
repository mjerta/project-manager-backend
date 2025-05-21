package nl.mpdev.project_manager_backend.services;

import jakarta.transaction.Transactional;
import nl.mpdev.project_manager_backend.dtos.user.UserInputOrUpdateDto;
import nl.mpdev.project_manager_backend.dtos.user.UserOutputDto;
import nl.mpdev.project_manager_backend.mappers.UserMapper;
import nl.mpdev.project_manager_backend.models.User;
import nl.mpdev.project_manager_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserOutputDto createUser(UserInputOrUpdateDto userInputDto) {
        User user = userMapper.ToEntity(userInputDto);
        userRepository.save(user);
        return userMapper.ToDto(user);
    }
}
