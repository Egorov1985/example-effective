package ru.egorov.effectiveexample.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.effectiveexample.dto.UserDtoInfo;
import ru.egorov.effectiveexample.dto.UserMappers;
import ru.egorov.effectiveexample.dto.UserRegistration;
import ru.egorov.effectiveexample.dto.UserView;
import ru.egorov.effectiveexample.exception.ResourceException;
import ru.egorov.effectiveexample.exception.UserNotFoundException;
import ru.egorov.effectiveexample.model.BankAccount;
import ru.egorov.effectiveexample.model.Email;
import ru.egorov.effectiveexample.model.Phone;
import ru.egorov.effectiveexample.model.User;
import ru.egorov.effectiveexample.repository.EmailRepository;
import ru.egorov.effectiveexample.repository.PhoneRepository;
import ru.egorov.effectiveexample.repository.UserRepository;
import ru.egorov.effectiveexample.util.CheckString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneRepository phonesRepository;
    private final EmailRepository emailRepository;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    @Transactional
    @Override
    public UserView createUser(UserRegistration userRegistration) {
        User user = new User();
        user.setLogin(userRegistration.getLogin());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.getEmails().add(new Email(userRegistration.getEmail(), true));
        user.getPhones().add(new Phone(userRegistration.getPhone(), true));

        BankAccount bankAccount = new BankAccount(userRegistration.getDeposit());
        user.setBankAccount(bankAccount);
        userRepository.save(user);

        logger.info("Create new user with login => {}", user.getLogin());

        return UserMappers.INSTANCE.userView(user);
    }

    @Transactional
    @Override
    public UserView addUserInfo(UserDtoInfo userDtoInfo, String login) {
        User userDB = getUser(login);
        userDB.setFirstName(userDtoInfo.getFirstName());
        userDB.setMiddleName(userDtoInfo.getMiddleName());
        userDB.setLastName(userDtoInfo.getLastName());
        userDB.setBirthday(LocalDate.parse(userDtoInfo.getBirthday(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        logger.info("Add additional information with user with login => {}", userDB.getLogin());
        userRepository.save(userDB);

        return UserMappers.INSTANCE.userAddInfo(userDtoInfo);
    }

    @Transactional
    @Override
    public boolean addOtherPhone(String phone, String login) {
        if (!isUserExistByPhoneNumber(phone)) {
            User user = getUser(login);
            user.getPhones().add(new Phone(phone, false));
            logger.info("Add additional phone number = '{}' for user with login => {}", phone, user.getLogin());
            userRepository.save(user);
            return true;
        }
        return false;

    }


    @Transactional
    @Override
    public boolean addOtherEmail(String email, String login) {
        if (!isUserExistByEmail(email)) {
            User user = getUser(login);
            user.getEmails().add(new Email(email, false));
            logger.info("Add additional email = '{}' for user with login => {}", email, user.getLogin());
            userRepository.save(user);
            return true;
        }
        return false;
    }


    @Transactional
    @Override
    public boolean deletePhone(String phoneNumber, String login) {
        User user = getUser(login);
        if (phonesRepository.existsPhonesByNumber(phoneNumber)) {
            int countPhones = phonesRepository.countPhoneByUserId(user.getId());
            if (countPhones > 1) {
                phonesRepository.deletePhoneByNumber(phoneNumber);
                logger.info("Delete phone number = '{}' for user with login => {}", phoneNumber, user.getLogin());
                return true;
            } else {
                throw new ResourceException("Нельзя удалить последний номер телефона!");
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteEmail(String email, String login) {
        User user = getUser(login);
        if (emailRepository.existsEmailByEmail(email)) {
            int countMails = emailRepository.countEmailByUserId(user.getId());
            if (countMails > 1) {
                emailRepository.deleteEmailByEmail(email);
                logger.info("Delete email = '{}' for user with login => {}", email, user.getLogin());
                return true;
            } else {
                throw new ResourceException("Нельзя удалить последний почтовый электронный адрес!");
            }
        }
        return false;
    }

    @Override
    public List<UserView> searchUser(String search) {
        List<User> users = new ArrayList<>();
        String id;

        if (CheckString.checkingStringForPhoneNumber(search)) {
            id = phonesRepository.getIdFromNumberPhone(search).orElseThrow(() -> new ResourceException("Пользователь с таким номером не зарегистрирован."));
            users.add(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!")));

        } else if (CheckString.checkingStringForEmail(search)) {
            id = emailRepository.getIdFromEmail(search).orElseThrow(() -> new ResourceException("Пользователь с такой электронной почтой не зарегистрирован."));
            users.add(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!")));
        } else if (CheckString.checkingStringForDate(search)) {
            users = userRepository.findUsersByBirthdayLessThan(LocalDate.parse(search.replaceAll("\\W", "."),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        } else if (CheckString.checkingStringForName(search)) {
            String lastName = "";
            String firstNAme = "";
            String middleName = "";

            String[] strings = search.split("\\s+");
            if (strings.length == 3) {
                lastName = strings[0].trim();
                firstNAme = strings[1].trim();
                middleName = strings[2].trim();
            } else if (strings.length == 2) {
                lastName = strings[0].trim();
                firstNAme = strings[1].trim();
            } else if (strings.length == 1) {
                lastName = strings[0].trim();
            }
            users = userRepository.findUsersByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCaseAndMiddleNameContainsIgnoreCase(lastName, firstNAme, middleName);
        }


        if (users.isEmpty()) {
            logger.info("Не найдено пользователей попадающие под поиск");
            throw new UserNotFoundException("Не найдено пользователей попадающие под критерии поиска.");
        }

        if (users.size() > 1)
            logger.info("Найдено {} пользователей соответсвуюшие поиску!", users.size());
        else
            logger.info("Найден один пользователь соответствующий поиску!");


        return users.stream().map(UserMappers.INSTANCE::userView).collect(Collectors.toList());
    }

    @Override
    public boolean isUserExistByLogin(String username) {
        return userRepository.existsUserByLogin(username);
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return emailRepository.existsEmailByEmail(email);
    }

    @Override
    public boolean isUserExistByPhoneNumber(String phoneNumber) {
        return phonesRepository.existsPhonesByNumber(phoneNumber);
    }


    private User getUser(String login) {
        User user = userRepository.findUserByLogin(login).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return user;
    }
}
