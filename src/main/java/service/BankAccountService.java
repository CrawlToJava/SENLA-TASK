package service;

import entity.BankAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BankAccountService {
    void save(BankAccount bankAccount);

    void delete(Long id);

    void update(BankAccount bankAccount);

    Optional<BankAccount> findById(Long id);

    List<BankAccount> findAll();

}
