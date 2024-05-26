package ru.egorov.effectiveexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.egorov.effectiveexample.model.Phone;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    int countPhoneByUserId(String id);
    void deletePhoneByNumber(String number);


    @Query(value = "SELECT user_id from Phones where number =:number", nativeQuery = true)
    Optional<String> getIdFromNumberPhone(@Param("number") String number);

    boolean existsPhonesByNumber(String number);

}
