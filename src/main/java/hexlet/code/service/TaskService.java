package hexlet.code.service;

import hexlet.code.dto.TaskCreateDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskFilter;
import hexlet.code.dto.TaskUpdateDto;

import java.util.List;

public interface TaskService {
    TaskDto getById(Long id);

    List<TaskDto> getAll(TaskFilter filter);

    TaskDto create(TaskCreateDto dto);

    TaskDto updatePartial(Long id, TaskUpdateDto dto);

    void delete(Long id);
}
