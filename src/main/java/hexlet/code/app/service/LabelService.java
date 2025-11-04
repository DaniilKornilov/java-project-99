package hexlet.code.app.service;

import hexlet.code.app.dto.LabelCreateDto;
import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.dto.LabelUpdateDto;
import hexlet.code.app.entity.Label;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.repository.LabelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static hexlet.code.app.constant.ErrorMessages.LABEL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public final class LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public LabelDto getById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LABEL_NOT_FOUND, id)));
        return labelMapper.toDto(label);
    }

    public List<LabelDto> getAll() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::toDto)
                .toList();
    }

    public LabelDto create(LabelCreateDto dto) {
        Label label = labelMapper.toEntity(dto);
        return labelMapper.toDto(labelRepository.save(label));
    }

    public LabelDto updatePartial(Long id, LabelUpdateDto dto) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LABEL_NOT_FOUND, id)));
        labelMapper.updateLabelFromDto(dto, label);
        return labelMapper.toDto(labelRepository.save(label));
    }

    public void delete(Long id) {
        if (!labelRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format(LABEL_NOT_FOUND, id));
        }
        labelRepository.deleteById(id);
    }
}
