package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserCreateDto;
import hexlet.code.app.dto.UserDto;
import hexlet.code.app.dto.UserUpdateDto;
import hexlet.code.app.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    private PasswordEncoder passwordEncoder;

    @Autowired
    public final void setPasswordEncoder(PasswordEncoder encoder) {
        this.passwordEncoder = encoder;
    }

    public abstract UserDto toDto(User user);

    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract User toEntity(UserCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

    @Named("encodePassword")
    protected final String encodePassword(String rawPassword) {
        return rawPassword == null ? null : passwordEncoder.encode(rawPassword);
    }
}
