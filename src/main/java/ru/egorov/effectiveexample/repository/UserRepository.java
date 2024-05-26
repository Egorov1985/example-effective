package ru.egorov.effectiveexample.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egorov.effectiveexample.model.User;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findUserByLogin(String login);

    List<User> findUsersByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCaseAndMiddleNameContainsIgnoreCase(String lastName, String firstName, String middleName);
    List<User> findUsersByBirthdayLessThan(LocalDate date);
    boolean existsUserByLogin(String login);




}
