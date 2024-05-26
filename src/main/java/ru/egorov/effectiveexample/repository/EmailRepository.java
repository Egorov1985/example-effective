package ru.egorov.effectiveexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.egorov.effectiveexample.model.Email;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {
    int countEmailByUserId(String id);
    void deleteEmailByEmail(String email);

    @Query(value = "select user_id from emails where email=:email", nativeQuery = true)
    Optional<String> getIdFromEmail(@Param("email") String email);

    boolean existsEmailByEmail(String email);
}
