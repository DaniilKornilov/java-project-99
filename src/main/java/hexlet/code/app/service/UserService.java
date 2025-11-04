package hexlet.code.app.service;

import hexlet.code.app.dto.UserCreateDto;
import hexlet.code.app.dto.UserDto;
import hexlet.code.app.dto.UserUpdateDto;
import hexlet.code.app.entity.User;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static hexlet.code.app.constant.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public final class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, id)));
        return userMapper.toDto(user);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto create(UserCreateDto dto) {
        User user = userMapper.toEntity(dto);
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto updatePartial(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, id)));
        userMapper.updateUserFromDto(dto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND, id));
        }
        userRepository.deleteById(id);
    }
}
