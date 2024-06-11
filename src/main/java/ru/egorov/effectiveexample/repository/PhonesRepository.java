package ru.egorov.effectiveexample.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.egorov.effectiveexample.model.Phone;

public interface PhonesRepository extends ReactiveCrudRepository<Phone, Long> {

    Mono<Integer> countPhoneByUserId(String id);
    Mono<Void> deletePhoneByNumber(String number);


    @Query(value = "SELECT user_id from Phones where number =:number")
    Mono<String> getUserIdFrom(@Param("number") String number);

    Mono<Boolean> existsPhonesByNumber(String number);

}
