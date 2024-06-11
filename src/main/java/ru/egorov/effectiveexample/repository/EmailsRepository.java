package ru.egorov.effectiveexample.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.egorov.effectiveexample.model.Email;

public interface EmailsRepository extends ReactiveCrudRepository<Email, Long> {


    Mono<Integer> countEmailsByUserId(String id);
    Mono<Void> deleteEmailsByEmail(String email);

    @Query(value = "select user_id from emails where email=:email")
    Mono<String> getIdFromEmail(@Param("email") String email);

    Mono<Boolean> existsEmailByEmail(String email);
}
