package service;

import entity.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountService {
    /**
     * This method saves the BankAccount entity to the database
     *
     * @param bankAccount the entity we need to save
     */
    void save(BankAccount bankAccount);

    /**
     * This method deletes the BankAccount entity by id from the database
     *
     * @param id of the entity to be checked for containment in the database
     */
    void delete(Long id);

    /**
     * This method updates the BankAccount entity in the database
     *
     * @param bankAccount the entity with which we need to update the entity in the database
     */
    void update(BankAccount bankAccount);

    /**
     * This method finds the BankAccount entity by id in the database
     *
     * @param id of the entity to be checked for containment in the database
     * @return the BankAccount entity wrapped in an optional
     */
    Optional<BankAccount> findById(Long id);

    /**
     * This method finds all BankAccount entities in the database
     *
     * @return the List of BankAccounts
     */
    List<BankAccount> findAll();

}
