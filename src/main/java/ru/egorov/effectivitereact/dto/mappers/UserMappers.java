package ru.egorov.effectivitereact.dto.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.egorov.effectivitereact.dto.UserDtoInfo;
import ru.egorov.effectivitereact.dto.UserRegistration;
import ru.egorov.effectivitereact.dto.UserView;
import ru.egorov.effectivitereact.model.User;

@Mapper
public interface UserMappers {
    UserMappers INSTANCE = Mappers.getMapper(UserMappers.class);

    UserView userView(UserRegistration userRegistration);
    UserView userView (User user);
    User user(UserDtoInfo userDtoInfo);

}
