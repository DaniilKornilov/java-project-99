package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDto;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.dto.TaskUpdateDto;
import hexlet.code.app.entity.Task;
import hexlet.code.app.entity.TaskStatus;
import hexlet.code.app.entity.User;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static hexlet.code.app.constant.ErrorMessages.TASK_NOT_FOUND;
import static hexlet.code.app.constant.ErrorMessages.TASK_STATUS_NOT_FOUND;
import static hexlet.code.app.constant.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public final class TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;

    public TaskDto getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(TASK_NOT_FOUND, id)));
        return taskMapper.toDto(task);
    }

    public List<TaskDto> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    public TaskDto create(TaskCreateDto dto) {
        Task task = taskMapper.toEntity(dto);

        Long assigneeId = dto.assigneeId();
        assignUserIfPresent(assigneeId, task);

        String slug = dto.status();
        linkTaskStatusBySlug(slug, task);

        return taskMapper.toDto(taskRepository.save(task));
    }

    public TaskDto updatePartial(Long id, TaskUpdateDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(TASK_NOT_FOUND, id)));
        taskMapper.updateTaskFromDto(dto, task);

        Long assigneeId = dto.assigneeId();
        assignUserIfPresent(assigneeId, task);

        String slug = dto.status();
        if (slug != null) {
            linkTaskStatusBySlug(slug, task);
        }

        return taskMapper.toDto(taskRepository.save(task));
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format(TASK_NOT_FOUND, id));
        }
        taskRepository.deleteById(id);
    }

    private void assignUserIfPresent(Long assigneeId, Task task) {
        if (assigneeId != null) {
            User assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND, assigneeId)));
            task.setAssignee(assignee);
        }
    }

    private void linkTaskStatusBySlug(String slug, Task task) {
        TaskStatus taskStatus = taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException(String.format(TASK_STATUS_NOT_FOUND, slug)));
        task.setTaskStatus(taskStatus);
    }
}
