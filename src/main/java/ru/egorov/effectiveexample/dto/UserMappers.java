package ru.egorov.effectiveexample.dto;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.egorov.effectiveexample.model.User;

@Mapper
public interface UserMappers {
    UserMappers INSTANCE = Mappers.getMapper(UserMappers.class);

    @Mapping(target = "bankAccount", source = "bankAccount")
    UserView userView(User user);

    UserView userAddInfo (UserDtoInfo userDtoInfo);

}
