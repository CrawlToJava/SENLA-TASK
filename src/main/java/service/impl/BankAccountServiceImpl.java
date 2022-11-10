package service.impl;

import entity.BankAccount;
import entity.BankAccountStatus;
import entity.Card;
import entity.User;
import exception.NoDataFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import repository.BankAccountRepository;
import repository.CardRepository;
import repository.UserRepository;
import service.BankAccountService;
import valid.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountRepository bankAccountRepository;

    private CardRepository cardRepository;

    private UserRepository userRepository;

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
