package hexlet.code.service;

import hexlet.code.dto.TaskCreateDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskFilter;
import hexlet.code.dto.TaskUpdateDto;
import hexlet.code.entity.Label;
import hexlet.code.entity.Task;
import hexlet.code.entity.TaskStatus;
import hexlet.code.entity.User;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskSpecification;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hexlet.code.constant.ErrorMessages.LABEL_NOT_FOUND;
import static hexlet.code.constant.ErrorMessages.TASK_NOT_FOUND;
import static hexlet.code.constant.ErrorMessages.TASK_STATUS_NOT_FOUND;
import static hexlet.code.constant.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public final class TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public TaskDto getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(TASK_NOT_FOUND, id)));
        return taskMapper.toDto(task);
    }

    public List<TaskDto> getAll(TaskFilter filter) {
        Specification<Task> spec = Specification.allOf(
                TaskSpecification.titleContains(filter.titleCont()),
                TaskSpecification.hasAssignee(filter.assigneeId()),
                TaskSpecification.hasStatus(filter.status()),
                TaskSpecification.hasLabel(filter.labelId())
        );
        return taskRepository.findAll(spec)
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

        Set<Long> taskLabelIds = dto.taskLabelIds();
        linkLabelsByIds(taskLabelIds, task);

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

        Set<Long> taskLabelIds = dto.taskLabelIds();
        linkLabelsByIds(taskLabelIds, task);

        return taskMapper.toDto(taskRepository.save(task));
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format(TASK_NOT_FOUND, id));
        }
        taskRepository.deleteById(id);
    }

    public void linkLabelsByIds(Set<Long> labelIds, Task task) {
        if (labelIds == null || labelIds.isEmpty()) {
            return;
        }

        List<Label> labels = labelRepository.findAllById(labelIds);
        labelIds.forEach(id -> {
            boolean exists = labels.stream().anyMatch(label -> label.getId().equals(id));
            if (!exists) {
                throw new IllegalArgumentException(String.format(LABEL_NOT_FOUND, id));
            }
        });

        task.setLabels(new HashSet<>(labels));
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
