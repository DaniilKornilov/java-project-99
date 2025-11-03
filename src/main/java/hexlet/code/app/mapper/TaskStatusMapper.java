package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskStatusCreateDto;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.dto.TaskStatusUpdateDto;
import hexlet.code.app.entity.TaskStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskStatusMapper {
    TaskStatusDto toDto(TaskStatus taskStatus);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TaskStatus toEntity(TaskStatusCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskStatusFromDto(TaskStatusUpdateDto dto, @MappingTarget TaskStatus taskStatus);
}
