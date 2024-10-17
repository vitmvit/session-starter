package by.vitikova.session.converter;

import by.vitikova.session.SessionDto;
import by.vitikova.session.model.entity.Session;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionConverter {

    SessionDto convert(Session source);

    Session convert(SessionDto source);
}