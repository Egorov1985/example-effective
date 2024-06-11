package ru.egorov.effectiveexample.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.egorov.effectiveexample.model.User;

public interface UsersRepository extends ReactiveCrudRepository<User, String> {

    Mono<User> findUsersByLogin(String login);

    Mono<Boolean> existsUserByLogin(String login);

 /*   @Query("SELECT exists(select users.login, emails.email, phones.number from users " +
            "left join emails on users.id = emails.user_id " +
            "left join phones on users.id = phones.user_id " +
            "where login ='':login or email =:email or number =:number)")
    Mono<Boolean> existsUserByLogin(@Param("login") String login, @Param("email") String email, @Param("number") String number);*/
}
