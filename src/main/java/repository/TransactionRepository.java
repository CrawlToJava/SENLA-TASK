package repository;

import entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    /**
     * This method saves the Transaction entity to the database
     *
     * @param transaction the entity we need to save
     */
    void save(Transaction transaction);

    /**
     * This method deletes the Transaction entity by id from the database
     *
     * @param id of the entity to be checked for containment in the database
     */
    void delete(Long id);

    /**
     * This method updates the Transaction entity in the database
     *
     * @param transaction the entity with which we need to update the entity in the database
     */
    void update(Transaction transaction);

    /**
     * This method finds the Transaction entity by id in the database
     *
     * @param id of the entity to be checked for containment in the database
     * @return the Transaction entity wrapped in an optional
     */
    Optional<Transaction> findById(Long id);

    /**
     * This method finds all Transaction entities in the database
     *
     * @return the List of Transactions
     */
    List<Transaction> findAll();
}
