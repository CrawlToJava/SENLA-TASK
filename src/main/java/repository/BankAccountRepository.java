package repository;

import entity.BankAccount;
import entity.Card;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository {
    void save(BankAccount bankAccount);

    void delete(Long id);

    void update(BankAccount bankAccount);

    Optional<BankAccount> findById(Long id);

    List<BankAccount> findAll();
}
