package hexlet.code.controller;

import hexlet.code.dto.LabelCreateDto;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.LabelUpdateDto;
import hexlet.code.service.LabelService;
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
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public final class LabelController {

    private final LabelService labelService;

    @GetMapping("/{id}")
    public ResponseEntity<LabelDto> getLabel(@PathVariable Long id) {
        return ResponseEntity.ok(labelService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<LabelDto>> getAllLabels() {
        List<LabelDto> labels = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @PostMapping
    public ResponseEntity<LabelDto> createLabel(@Valid @RequestBody LabelCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labelService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelDto> updateLabel(@PathVariable Long id,
                                                @Valid @RequestBody LabelUpdateDto dto) {
        return ResponseEntity.ok(labelService.updatePartial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        labelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
