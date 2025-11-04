package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDto;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.TaskStatusUpdateDto;
import hexlet.code.entity.TaskStatus;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static hexlet.code.constant.ErrorMessages.TASK_STATUS_NOT_FOUND;

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
