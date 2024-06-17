package ru.egorov.effectiveexample.repository;

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.egorov.effectiveexample.model.User;

import java.time.LocalDate;

@EnableR2dbcRepositories
public interface UsersRepository extends ReactiveCrudRepository<User, String>, ReactiveQueryByExampleExecutor<User> {

    Mono<User> findUsersByLogin(String login);

    Mono<Boolean> existsUserByLogin(String login);


    Flux<User> findByBirthdayLessThan(LocalDate date);

    /*   @Query("SELECT exists(select users.login, emails.email, phones.number from users " +
            "left join emails on users.id = emails.user_id " +
            "left join phones on users.id = phones.user_id " +
            "where login ='':login or email =:email or number =:number)")
    Mono<Boolean> existsUserByLogin(@Param("login") String login, @Param("email") String email, @Param("number") String number);*/
}
