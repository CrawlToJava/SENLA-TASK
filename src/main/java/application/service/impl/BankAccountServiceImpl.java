package application.service.impl;

import application.entity.BankAccount;
import application.repository.BankAccountRepository;
import application.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("bankAccountService")
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

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
