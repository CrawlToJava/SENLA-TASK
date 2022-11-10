package service.impl;

import entity.BankAccount;
import lombok.AllArgsConstructor;
import repository.BankAccountRepository;
import service.BankAccountService;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountRepository bankAccountRepository;

    @Override
    public void save(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void delete(Long id) {
        bankAccountRepository.delete(id);
    }

    @Override
    public void update(BankAccount bankAccount) {
        bankAccountRepository.update(bankAccount);
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }
}
