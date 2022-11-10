package repository;

import entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction);

    void delete(Long id);

    void update(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findAll();
}
