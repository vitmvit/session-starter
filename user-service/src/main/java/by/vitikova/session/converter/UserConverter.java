package by.vitikova.session.converter;

import by.vitikova.session.model.dto.UserCreateDto;
import by.vitikova.session.model.dto.UserDto;
import by.vitikova.session.model.dto.UserOpenTimeDto;
import by.vitikova.session.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {

    User convert(UserCreateDto request);

    UserCreateDto convertToCreateDto(UserOpenTimeDto request);

    UserDto convert(User user);
}