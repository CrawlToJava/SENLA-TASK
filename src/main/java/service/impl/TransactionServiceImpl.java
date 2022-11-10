package service.impl;

import entity.*;
import exception.NoDataFoundException;
import repository.*;
import service.TransactionService;
import valid.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class TransactionServiceImpl implements TransactionService {

    private BankAccountRepository bankAccountRepository;

    private UserRepository userRepository;

    private CardRepository cardRepository;

    private TransactionRepository transactionRepository;

    private CashMachineRepository cashMachineRepository;

    @Override
    public void logIn(Long cardNumber, Integer pinCode, Long bankAccountId, Long userId, Long cardId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NoDataFoundException("Карты с таким id не существует"));
        if (Valid.isCardNumberCorrect(cardNumber, card) && Valid.isPinCodeCorrect(pinCode, card)) {
            bankAccount.setBankAccountStatus(BankAccountStatus.AUTHORIZED);
            System.out.println("Вы успешно зашли в аккаунт");
        }
    }

    @Override
    public void checkBalance(Long bankAccountId, Long transactionId, Long cashMachineId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository.findById(cashMachineId).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        if (Valid.isAccountAuthorised(bankAccount) && Valid.isCashMachineOpen(cashMachine)) {
            System.out.println("Ваш баланс: " + bankAccount.getAmountOfMoney());
            transactionRepository.save(new Transaction(transactionId, bankAccount, cashMachine,TransactionStatus.CLOSE));
        }
    }

    @Override
    public void getMoney(Long bankAccountId, Long cashMachineId, Long transactionId, BigDecimal howMuchMoneyWithdraw) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        CashMachine cashMachine = cashMachineRepository.findById(cashMachineId).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        if (Valid.isAccountAuthorised(bankAccount)
                && Valid.isEnoughMoneyOnTheBalance(bankAccount, howMuchMoneyWithdraw)
                && Valid.isWithdrawMoneyNotGreaterThanCashMachineLimit(howMuchMoneyWithdraw, cashMachine)
                && Valid.isCashMachineOpen(cashMachine)) {
            bankAccountRepository.update(new BankAccount(bankAccount.getId()
                    , bankAccount.getUser()
                    , bankAccount.getAmountOfMoney().subtract(howMuchMoneyWithdraw)
                    , bankAccount.getCard()
                    , bankAccount.getBankAccountStatus()));
            transactionRepository.save(new Transaction(transactionId, bankAccount, cashMachine, TransactionStatus.CLOSE));
        }
    }

    @Override
    public void replenishBalance(Long bankAccountId, Long userId, Long cardId) {

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
