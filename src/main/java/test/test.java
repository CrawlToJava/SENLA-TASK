package test;

import entity.*;
import exception.NoDataFoundException;
import repository.*;
import repository.impl.*;
import service.*;
import service.impl.*;

import java.math.BigDecimal;

public class test {
    public static void main(String[] args) {
        BankAccountRepository bankAccountRepository = new BankAccountRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        CashMachineRepository cashMachineRepository = new CashMachineRepositoryImpl();
        CardRepository cardRepository = new CardRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();

        BankAccountService bankAccountService = new BankAccountServiceImpl(bankAccountRepository);
        TransactionService transactionService = new TransactionServiceImpl(bankAccountRepository, transactionRepository, cashMachineRepository);
        CashMachineService cashMachineService = new CashMachineServiceImpl(cashMachineRepository);
        UserService userService = new UserServiceImpl(userRepository);
        CardService cardService = new CardServiceImpl(cardRepository);
        cashMachineService.save(new CashMachine(1L, "Фабричная 26", new BigDecimal(1000000000L), CashMachineStatus.OPEN));
        userService.save(new User(1L, "Alana", "Spurging", UserStatus.FRIENDLY));
        cardService.save(new Card(1L, 1111, 1111111111111111L, CardStatus.AVAILABLE));
        bankAccountService.save(new BankAccount(1L, userService.findById(1L)
                .orElseThrow(() -> new NoDataFoundException("Пользователь не найден")), new BigDecimal(122000L), cardService.findById(1L).orElseThrow(() -> new NoDataFoundException("Карта не найдена")), BankAccountStatus.NOT_AUTHORIZED));
        transactionService.logIn(1111111111111111L, 1111, 1L, 1L, 1L);
        System.out.println(transactionService.findById(1L));
        transactionService.checkBalance(1L, 2L, 1L);
        System.out.println(transactionService.findById(2L));
        transactionService.getMoney(1L, 1L, 3L, new BigDecimal(5600L));
        System.out.println(transactionService.findById(3L));
        transactionService.putMoney(1L, 1L, 4L, new BigDecimal(1000000L));
        System.out.println(transactionService.findById(4L));
    }
}
