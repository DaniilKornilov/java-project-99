package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusCreateDto;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.dto.TaskStatusUpdateDto;
import hexlet.code.app.service.TaskStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@RequiredArgsConstructor
public final class TaskStatusController {

    private final TaskStatusService taskStatusService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusDto> getTaskStatus(@PathVariable Long id) {
        return ResponseEntity.ok(taskStatusService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskStatusDto>> getAllTaskStatuses() {
        List<TaskStatusDto> taskStatuses = taskStatusService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskStatuses.size()))
                .body(taskStatuses);
    }

    @PostMapping
    public ResponseEntity<TaskStatusDto> createTaskStatus(@Valid @RequestBody TaskStatusCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskStatusService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusDto> updateTaskStatus(@PathVariable Long id,
                                                          @Valid @RequestBody TaskStatusUpdateDto dto) {
        return ResponseEntity.ok(taskStatusService.updatePartial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long id) {
        taskStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
