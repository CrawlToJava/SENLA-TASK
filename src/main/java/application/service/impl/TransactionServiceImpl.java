package application.service.impl;

import application.entity.*;
import application.exception.NoDataFoundException;
import application.repository.BankAccountRepository;
import application.repository.CashMachineRepository;
import application.repository.TransactionRepository;
import application.service.TransactionService;
import application.valid.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;

    private final CashMachineRepository cashMachineRepository;

    @Override
    public void logIn(Long cardNumber, Integer pinCode, Long bankAccountId, Long cashMachineId, Long transactionId) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Bank account doesn't exist"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Cash machine doesn't exist"));
        if (Valid.isCardNumberCorrect(cardNumber, bankAccount.getCard())
                && Valid.isPinCodeCorrect(pinCode, bankAccount.getCard())
                && Valid.isCashMachineOpen(cashMachine)
                && Valid.isCardAvailable(bankAccount)
                && Valid.isAccountNotAuthorized(bankAccount, new BankAccountServiceImpl(bankAccountRepository))
                && Valid.isUserFriendly(bankAccount)) {
            bankAccountRepository.update(new BankAccount(bankAccount.getId()
                    , bankAccount.getAmountOfMoney()
                    , bankAccount.getCard()
                    , bankAccount.getUser()
                    , BankAccountStatus.AUTHORIZED));
            transactionRepository.save(new Transaction(transactionId
                    , bankAccount
                    , cashMachine
                    , "Log in"
                    , TransactionStatus.CLOSE));
            System.out.println("You have successfully logged in");
        }
    }

    @Override
    public void logOut(Long bankAccountId, Long cashMachineId, Long transactionId) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Bank account doesn't exist"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Cash machine doesn't exist"));
        if (Valid.isCashMachineOpen(cashMachine)
                && Valid.isCardAvailable(bankAccount)
                && Valid.isAccountAuthorised(bankAccount)) {
            bankAccountRepository.update(new BankAccount(bankAccount.getId()
                    , bankAccount.getAmountOfMoney()
                    , bankAccount.getCard()
                    , bankAccount.getUser()
                    , BankAccountStatus.NOT_AUTHORIZED));
            transactionRepository.save(new Transaction(transactionId
                    , bankAccount
                    , cashMachine
                    , "Log out"
                    , TransactionStatus.CLOSE));
            System.out.println("You have successfully logged out");
        }
    }

    @Override
    public void checkBalance(Long bankAccountId, Long transactionId, Long cashMachineId) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Bank account doesn't exist"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Cash machine doesn't exist"));
        if (Valid.isAccountAuthorised(bankAccount)
                && Valid.isCashMachineOpen(cashMachine)
                && Valid.isCardAvailable(bankAccount)
                && Valid.isUserFriendly(bankAccount)) {
            transactionRepository.save(new Transaction(transactionId
                    , bankAccount
                    , cashMachine
                    , "Balance check"
                    , TransactionStatus.CLOSE));
            System.out.println("Your balance: " + bankAccount.getAmountOfMoney());
        }
    }

    @Override
    public void getMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoneyWithdraw) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Bank account doesn't exist"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Cash machine doesn't exist"));
        if (Valid.isAccountAuthorised(bankAccount)
                && Valid.isEnoughMoneyOnTheBalance(bankAccount, howMuchMoneyWithdraw, new BankAccountServiceImpl(bankAccountRepository))
                && Valid.isWithdrawMoneyNotGreaterThanCashMachineLimit(howMuchMoneyWithdraw, cashMachine, bankAccount, new BankAccountServiceImpl(bankAccountRepository))
                && Valid.isCashMachineOpen(cashMachine)
                && Valid.isCardAvailable(bankAccount)
                && Valid.isUserFriendly(bankAccount)) {
            bankAccountRepository.update(new BankAccount(bankAccount.getId()
                    , bankAccount.getAmountOfMoney().subtract(howMuchMoneyWithdraw)
                    , bankAccount.getCard()
                    , bankAccount.getUser()
                    , BankAccountStatus.AUTHORIZED));
            transactionRepository.save(new Transaction(transactionId
                    , bankAccount
                    , cashMachine
                    , "Withdrawing money from the account"
                    , TransactionStatus.CLOSE));
            System.out.println("You have successfully withdrew money from the account");
        }
    }

    @Override
    public void putMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoneyPutOnTheBalance) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Bank account doesn't exist"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Cash machine doesn't exist"));
        if (Valid.isAccountAuthorised(bankAccount)
                && Valid.isCashMachineOpen(cashMachine)
                && Valid.isPutMoneyAvailable(howMuchMoneyPutOnTheBalance, bankAccount, new BankAccountServiceImpl(bankAccountRepository))
                && Valid.isCardAvailable(bankAccount)
                && Valid.isUserFriendly(bankAccount)) {
            bankAccountRepository.update(new BankAccount(bankAccount.getId()
                    , bankAccount.getAmountOfMoney().add(howMuchMoneyPutOnTheBalance)
                    , bankAccount.getCard()
                    , bankAccount.getUser()
                    , BankAccountStatus.AUTHORIZED));
            transactionRepository.save(new Transaction(transactionId
                    , bankAccount
                    , cashMachine
                    , "Depositing money into the account"
                    , TransactionStatus.CLOSE));
            System.out.println("You have successfully deposited money into your account");
        }
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void delete(Long id) {
        transactionRepository.delete(id);
    }

    @Override
    public void update(Transaction transaction) {
        transactionRepository.update(transaction);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
