package ru.egorov.effectiveexample.service;

public interface DepositService {

     boolean transferMoneyToUser(String login, String numberPhone, Double depositTransfer);
}
