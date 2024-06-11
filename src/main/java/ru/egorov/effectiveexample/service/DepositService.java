package ru.egorov.effectiveexample.service;

import reactor.core.publisher.Mono;

public interface DepositService {

    Mono<Boolean> transferMoneyToUser(String login, String numberPhone, Double depositTransfer);

}
