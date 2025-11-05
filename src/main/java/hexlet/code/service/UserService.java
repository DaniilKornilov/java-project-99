package hexlet.code.service;

import hexlet.code.dto.UserCreateDto;
import hexlet.code.dto.UserDto;
import hexlet.code.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserDto getById(Long id);

    List<UserDto> getAll();

    UserDto create(UserCreateDto dto);

    UserDto updatePartial(Long id, UserUpdateDto dto);

    void delete(Long id);
}
