package repository.impl;

import entity.Transaction;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final List<Transaction> transactionList = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        if (transactionList.stream().noneMatch(transaction1 -> transaction1.getId().equals(transaction.getId()))) {
            transactionList.add(transaction);
        } else {
            throw new NotAvailableException("Транзакция с таким id уже существует");
        }
    }

    @Override
    public void delete(Long id) {
        transactionList.remove(findById(id).orElseThrow(() -> new NoDataFoundException("Транзакции с таким id не существует")));
    }

    @Override
    public void update(Transaction transaction) {
        Transaction updatedTransaction = findById(transaction.getId()).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        updatedTransaction.setId(transaction.getId());
        updatedTransaction.setCashMachine(transaction.getCashMachine());
        updatedTransaction.setBankAccount(transaction.getBankAccount());
        updatedTransaction.setTransactionStatus(transaction.getTransactionStatus());
        updatedTransaction.setDescription(transaction.getDescription());
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionList.stream().filter(transaction -> transaction.getId().equals(id)).findFirst();
    }

    @Override
    public List<Transaction> findAll() {
        return transactionList;
    }
}
