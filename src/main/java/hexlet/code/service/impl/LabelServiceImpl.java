package hexlet.code.service.impl;

import hexlet.code.dto.LabelCreateDto;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.LabelUpdateDto;
import hexlet.code.entity.Label;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static hexlet.code.constant.ErrorMessages.LABEL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public final class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    public LabelDto getById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LABEL_NOT_FOUND, id)));
        return labelMapper.toDto(label);
    }

    @Override
    public List<LabelDto> getAll() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::toDto)
                .toList();
    }

    @Override
    public LabelDto create(LabelCreateDto dto) {
        Label label = labelMapper.toEntity(dto);
        return labelMapper.toDto(labelRepository.save(label));
    }

    @Override
    public LabelDto updatePartial(Long id, LabelUpdateDto dto) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LABEL_NOT_FOUND, id)));
        labelMapper.updateLabelFromDto(dto, label);
        return labelMapper.toDto(labelRepository.save(label));
    }

    @Override
    public void delete(Long id) {
        if (!labelRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format(LABEL_NOT_FOUND, id));
        }
        labelRepository.deleteById(id);
    }
}
