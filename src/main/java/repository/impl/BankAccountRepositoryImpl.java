package repository.impl;

import entity.BankAccount;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import repository.BankAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BankAccountRepositoryImpl implements BankAccountRepository {
    private final List<BankAccount> bankAccountList = new ArrayList<>();

    @Override
    public void save(BankAccount bankAccount) {
        if (bankAccountList.stream().noneMatch(bankAccount1 -> bankAccount1.getId().equals(bankAccount.getId()))) {
            bankAccountList.add(bankAccount);
        } else {
            throw new NotAvailableException("Банковский аккаунт с таким id уже существует");
        }
    }

    @Override
    public void delete(Long id) {
        bankAccountList.remove(bankAccountList.stream()
                .filter(bankAccount -> bankAccount.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует")));
    }

    @Override
    public void update(BankAccount bankAccount) {
        BankAccount updatedBankAccount = bankAccountList.stream()
                .filter(bankAccount1 -> bankAccount1.getId().equals(bankAccount.getId()))
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("Банковского аккаунт с таким id не существует"));
        updatedBankAccount.setId(bankAccount.getId());
        updatedBankAccount.setCard(bankAccount.getCard());
        updatedBankAccount.setUser(bankAccount.getUser());
        updatedBankAccount.setAmountOfMoney(bankAccount.getAmountOfMoney());
        updatedBankAccount.setBankAccountStatus(bankAccount.getBankAccountStatus());
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return bankAccountList.stream().filter(bankAccount -> bankAccount.getId().equals(id)).findFirst();
    }

    @Override
    public List<BankAccount> findAll() {
        return bankAccountList;
    }
}
