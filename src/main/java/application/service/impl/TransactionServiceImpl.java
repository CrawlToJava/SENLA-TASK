package application.service.impl;

import application.entity.*;
import application.exception.NoDataFoundException;
import application.repository.BankAccountRepository;
import application.repository.CashMachineRepository;
import application.repository.TransactionRepository;
import application.service.TransactionService;
import application.valid.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;

    private final CashMachineRepository cashMachineRepository;

    @Autowired
    public TransactionServiceImpl(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository, CashMachineRepository cashMachineRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.cashMachineRepository = cashMachineRepository;
    }

    @Override
    public void logIn(Long cardNumber, Integer pinCode, Long bankAccountId, Long cashMachineId, Long transactionId) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
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
                    , "Авторизация"
                    , TransactionStatus.CLOSE));
            System.out.println("Вы успешно зашли в аккаунт");
        }
    }

    @Override
    public void logOut(Long bankAccountId, Long cashMachineId, Long transactionId) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
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
                    , "Деавторизация"
                    , TransactionStatus.CLOSE));
            System.out.println("Вы успешно вышли из аккаунта");
        }
    }

    @Override
    public void checkBalance(Long bankAccountId, Long transactionId, Long cashMachineId) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        if (Valid.isAccountAuthorised(bankAccount)
                && Valid.isCashMachineOpen(cashMachine)
                && Valid.isCardAvailable(bankAccount)
                && Valid.isUserFriendly(bankAccount)) {
            transactionRepository.save(new Transaction(transactionId
                    , bankAccount
                    , cashMachine
                    , "Проверка баланса"
                    , TransactionStatus.CLOSE));
            System.out.println("Ваш баланс: " + bankAccount.getAmountOfMoney());
        }
    }

    @Override
    public void getMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoneyWithdraw) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
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
                    , "Снятие денег со счета"
                    , TransactionStatus.CLOSE));
            System.out.println("Вы успешно сняли со счета " + howMuchMoneyWithdraw);
        }
    }

    @Override
    public void putMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoneyPutOnTheBalance) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository
                .findById(cashMachineId)
                .orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
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
                    , "Пополнение счета"
                    , TransactionStatus.CLOSE));
            System.out.println("Вы успешно пополнили счет на сумму " + howMuchMoneyPutOnTheBalance);
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
