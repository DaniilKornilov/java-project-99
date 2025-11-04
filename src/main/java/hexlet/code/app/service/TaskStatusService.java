package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusCreateDto;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.dto.TaskStatusUpdateDto;
import hexlet.code.app.entity.TaskStatus;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static hexlet.code.app.constant.ErrorMessages.TASK_STATUS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public final class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;

    public TaskStatusDto getById(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(TASK_STATUS_NOT_FOUND, id)));
        return taskStatusMapper.toDto(taskStatus);
    }

    public List<TaskStatusDto> getAll() {
        return taskStatusRepository.findAll()
                .stream()
                .map(taskStatusMapper::toDto)
                .toList();
    }

    public TaskStatusDto create(TaskStatusCreateDto dto) {
        TaskStatus taskStatus = taskStatusMapper.toEntity(dto);
        return taskStatusMapper.toDto(taskStatusRepository.save(taskStatus));
    }

    public TaskStatusDto updatePartial(Long id, TaskStatusUpdateDto dto) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(TASK_STATUS_NOT_FOUND, id)));
        taskStatusMapper.updateTaskStatusFromDto(dto, taskStatus);
        return taskStatusMapper.toDto(taskStatusRepository.save(taskStatus));
    }

    public void delete(Long id) {
        if (!taskStatusRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format(TASK_STATUS_NOT_FOUND, id));
        }
        taskStatusRepository.deleteById(id);
    }
}
