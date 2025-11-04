package hexlet.code.app.mapper;

import hexlet.code.app.dto.LabelCreateDto;
import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.dto.LabelUpdateDto;
import hexlet.code.app.entity.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelDto toDto(Label label);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Label toEntity(LabelCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateLabelFromDto(LabelUpdateDto dto, @MappingTarget Label label);
}
