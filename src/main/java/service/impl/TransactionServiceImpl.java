package service.impl;

import entity.*;
import exception.NoDataFoundException;
import lombok.AllArgsConstructor;
import repository.BankAccountRepository;
import repository.CashMachineRepository;
import repository.TransactionRepository;
import service.TransactionService;
import valid.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private BankAccountRepository bankAccountRepository;

    private TransactionRepository transactionRepository;

    private CashMachineRepository cashMachineRepository;

    @Override
    public void logIn(Long cardNumber, Integer pinCode, Long bankAccountId, Long cashMachineId, Long transactionId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository.findById(cashMachineId).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        if (Valid.isCardNumberCorrect(cardNumber, bankAccount.getCard())
                && Valid.isPinCodeCorrect(pinCode, bankAccount.getCard())
                && Valid.isCashMachineOpen(cashMachine)
                && Valid.isCardAvailable(bankAccount)) {
            bankAccountRepository.update(new BankAccount(bankAccount.getId()
                    , bankAccount.getUser()
                    , bankAccount.getAmountOfMoney()
                    , bankAccount.getCard(), BankAccountStatus.AUTHORIZED));
            System.out.println("Вы успешно зашли в аккаунт");
            transactionRepository.save(new Transaction(transactionId, bankAccount, cashMachine, "Авторизация", TransactionStatus.CLOSE));
        }
    }

    @Override
    public void checkBalance(Long bankAccountId, Long transactionId, Long cashMachineId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository.findById(cashMachineId).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        if (Valid.isAccountAuthorised(bankAccount)
                && Valid.isCashMachineOpen(cashMachine)
                && Valid.isCardAvailable(bankAccount)
                && Valid.isUserFriendly(bankAccount)) {
            System.out.println("Ваш баланс: " + bankAccount.getAmountOfMoney());
            transactionRepository.save(new Transaction(transactionId, bankAccount, cashMachine, "Проверка баланса", TransactionStatus.CLOSE));
        }
    }

    @Override
    public void getMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoneyWithdraw) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository.findById(cashMachineId).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        if (Valid.isAccountAuthorised(bankAccount)
                && Valid.isEnoughMoneyOnTheBalance(bankAccount, howMuchMoneyWithdraw)
                && Valid.isWithdrawMoneyNotGreaterThanCashMachineLimit(howMuchMoneyWithdraw, cashMachine)
                && Valid.isCashMachineOpen(cashMachine)
                && Valid.isCardAvailable(bankAccount)
                && Valid.isUserFriendly(bankAccount)) {
            bankAccountRepository.update(new BankAccount(bankAccount.getId()
                    , bankAccount.getUser()
                    , bankAccount.getAmountOfMoney().subtract(howMuchMoneyWithdraw)
                    , bankAccount.getCard()
                    , bankAccount.getBankAccountStatus()));
            System.out.println("Вы успешно сняли со счета " + howMuchMoneyWithdraw);
            transactionRepository.save(new Transaction(transactionId, bankAccount, cashMachine, "Снятие денег со счета", TransactionStatus.CLOSE));
        }
    }

    @Override
    public void putMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoneyPutOnTheBalance) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository.findById(cashMachineId).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        if (Valid.isAccountAuthorised(bankAccount)
                && Valid.isCashMachineOpen(cashMachine)
                && Valid.isPutMoneyAvailable(howMuchMoneyPutOnTheBalance)
                && Valid.isCardAvailable(bankAccount)
                && Valid.isUserFriendly(bankAccount)) {
            bankAccountRepository.update(new BankAccount(bankAccount.getId()
                    , bankAccount.getUser()
                    , bankAccount.getAmountOfMoney().add(howMuchMoneyPutOnTheBalance)
                    , bankAccount.getCard()
                    , bankAccount.getBankAccountStatus()));
            System.out.println("Вы успешно пополнили счет на сумму " + howMuchMoneyPutOnTheBalance);
            transactionRepository.save(new Transaction(transactionId, bankAccount, cashMachine, "Пополнение счета", TransactionStatus.CLOSE));
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
