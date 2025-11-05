package hexlet.code.service;

import hexlet.code.dto.LabelCreateDto;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.LabelUpdateDto;

import java.util.List;

public interface LabelService {
    LabelDto getById(Long id);

    List<LabelDto> getAll();

    LabelDto create(LabelCreateDto dto);

    LabelDto updatePartial(Long id, LabelUpdateDto dto);

    void delete(Long id);
}
