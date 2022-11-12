package repository.impl;

import au.com.bytecode.opencsv.CSVWriter;
import entity.BankAccount;
import entity.CashMachine;
import entity.Transaction;
import entity.TransactionStatus;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import repository.BankAccountRepository;
import repository.CashMachineRepository;
import repository.TransactionRepository;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionRepositoryImpl implements TransactionRepository {
    private List<Transaction> transactionList = new ArrayList<>();

    private final BankAccountRepository bankAccountRepository;

    private final CashMachineRepository cashMachineRepository;

    public TransactionRepositoryImpl(BankAccountRepository bankAccountRepository, CashMachineRepository cashMachineRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.cashMachineRepository = cashMachineRepository;
    }

    private final String dataBase = "C:\\Users\\User\\IdeaProjects\\SENLA-TECHNICAL-TASK\\src\\main\\java\\data\\Transactions.csv";

    @Override
    public void save(Transaction transaction) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(dataBase, true))) {
            transactionList = findAll();
            if (transactionList.stream().noneMatch(transaction1 -> transaction1.getId().equals(transaction.getId()))) {
                transactionList.add(transaction);
                String[] record = {String.valueOf(transaction.getId())
                        , String.valueOf(transaction.getBankAccount().getId())
                        , String.valueOf(transaction.getCashMachine().getId())
                        , String.valueOf(transaction.getDescription())
                        , String.valueOf(transaction.getTransactionStatus())};
                writer.writeNext(record);
            } else {
                throw new NotAvailableException("Транзакция с таким id уже существует");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        transactionList = findAll();
        transactionList.remove(findById(id)
                .orElseThrow(() -> new NoDataFoundException("Транзакции с таким id не существует")));
        refreshAfterDeleting(transactionList);
    }

    @Override
    public void update(Transaction transaction) {
        Transaction updatedTransaction = findById(transaction.getId())
                .orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        updatedTransaction.setId(transaction.getId());
        updatedTransaction.setCashMachine(transaction.getCashMachine());
        updatedTransaction.setBankAccount(transaction.getBankAccount());
        updatedTransaction.setTransactionStatus(transaction.getTransactionStatus());
        updatedTransaction.setDescription(transaction.getDescription());
        delete(transaction.getId());
        save(updatedTransaction);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return findAll().stream().filter(transaction -> transaction.getId().equals(id)).findFirst();
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();
        Path path = Paths.get(dataBase);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                Transaction transaction = createTransaction(attributes);
                list.add(transaction);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Transaction createTransaction(String[] metadata) {
        Long id = Long.parseLong(metadata[0].replace("\"", ""));
        BankAccount bankAccount = bankAccountRepository
                .findById(Long.parseLong(metadata[1].replace("\"", "")))
                .orElseThrow(() -> new NoDataFoundException("Банковский аккаунт с таким id не найден"));
        CashMachine cashMachine = cashMachineRepository
                .findById(Long.parseLong(metadata[2].replace("\"", "")))
                .orElseThrow(() -> new NoDataFoundException("Банкомат с таким id не найден"));
        String description = metadata[3];
        TransactionStatus transactionStatus = TransactionStatus.valueOf(metadata[4].replace("\"", ""));
        return new Transaction(id, bankAccount, cashMachine, description, transactionStatus);
    }

    private void refreshAfterDeleting(List<Transaction> list) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(dataBase, true));
             PrintWriter writer = new PrintWriter(dataBase)) {
            writer.print("");
            for (Transaction transaction : list) {
                String[] record = {String.valueOf(transaction.getId())
                        , String.valueOf(transaction.getBankAccount().getId())
                        , String.valueOf(transaction.getCashMachine().getId())
                        , String.valueOf(transaction.getDescription())
                        , String.valueOf(transaction.getTransactionStatus())};
                csvWriter.writeNext(record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
