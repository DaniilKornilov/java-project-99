package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDto;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.TaskStatusUpdateDto;

import java.util.List;

public interface TaskStatusService {
    TaskStatusDto getById(Long id);

    List<TaskStatusDto> getAll();

    TaskStatusDto create(TaskStatusCreateDto dto);

    TaskStatusDto updatePartial(Long id, TaskStatusUpdateDto dto);

    void delete(Long id);
}
