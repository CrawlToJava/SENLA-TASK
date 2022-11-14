package service;

import entity.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    void save(Transaction transaction);

    void delete(Long id);

    void update(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findAll();

    void checkBalance(Long bankAccountId, Long transactionId, Long cashMachineId);

    void getMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoney);

    void putMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoneyPutOnTheBalance);

    void logIn(Long cardNumber, Integer pinCode, Long bankAccountId, Long cashMachineId, Long transactionId);

    void logOut(Long bankAccountId, Long cashMachineId, Long transactionId);
}
