package service;

import entity.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
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

    /**
     * This method prints the account balance
     *
     * @param bankAccountId the id of the BankAccount
     * @param transactionId the id of the Transaction
     * @param cashMachineId the id of the CashMachine
     */
    void checkBalance(Long bankAccountId, Long transactionId, Long cashMachineId);

    /**
     * This method withdraws money from the account balance
     *
     * @param bankAccountId the id of the BankAccount
     * @param cashMachineId the id of the CashMachine
     * @param transactionId the id of the Transaction
     * @param howMuchMoney  how much money to withdraw from the account balance
     */
    void getMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoney);

    /**
     * This method credits money to the account balance
     *
     * @param bankAccountId               the id of the BankAccount
     * @param cashMachineId               the id of the CashMachine
     * @param transactionId               the id of the Transaction
     * @param howMuchMoneyPutOnTheBalance how much money to put from the account balance
     */
    void putMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoneyPutOnTheBalance);

    /**
     * This method logins the BankAccount
     *
     * @param cardNumber    the card number
     * @param pinCode       the card pin
     * @param bankAccountId the id of the BankAccount
     * @param cashMachineId the id of the CashMachine
     * @param transactionId the id of the Transaction
     */
    void logIn(Long cardNumber, Integer pinCode, Long bankAccountId, Long cashMachineId, Long transactionId);

    /**
     * This method logouts the BankAccount
     *
     * @param bankAccountId the id of the BankAccount
     * @param cashMachineId the id of the CashMachine
     * @param transactionId the id of the Transaction
     */
    void logOut(Long bankAccountId, Long cashMachineId, Long transactionId);
}
