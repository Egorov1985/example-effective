package ru.egorov.effectiveexample.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.egorov.effectiveexample.dto.UserDtoInfo;
import ru.egorov.effectiveexample.dto.UserRegistration;
import ru.egorov.effectiveexample.dto.UserView;
import ru.egorov.effectiveexample.model.User;

@Mapper
public interface UserMappers {
    UserMappers INSTANCE = Mappers.getMapper(UserMappers.class);

    UserView userView(UserRegistration userRegistration);
    UserView userView (User user);
    User user(UserDtoInfo userDtoInfo);

}
