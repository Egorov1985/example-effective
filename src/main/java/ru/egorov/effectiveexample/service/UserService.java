package ru.egorov.effectiveexample.service;

import ru.egorov.effectiveexample.dto.UserDtoInfo;
import ru.egorov.effectiveexample.dto.UserRegistration;
import ru.egorov.effectiveexample.dto.UserView;

import java.util.List;

public interface UserService {

     UserView createUser(UserRegistration userRegistration);
     UserView addUserInfo(UserDtoInfo userDtoInfo, String login);
     boolean addOtherPhone(String phone, String login);
     boolean addOtherEmail(String email, String login);
     boolean deletePhone(String phoneNumber, String login);
     boolean deleteEmail(String email, String login);
     List<UserView> searchUser(String search);

     boolean isUserExistByLogin(String username);
     boolean isUserExistByEmail(String email);
     boolean isUserExistByPhoneNumber(String phoneNumber);


}
