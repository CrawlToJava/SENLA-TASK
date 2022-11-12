package test;

import repository.*;
import repository.impl.*;
import service.*;
import service.impl.*;

import java.math.BigDecimal;

public class test {
    public static void main(String[] args) {
        CardRepository cardRepository = new CardRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        BankAccountRepository bankAccountRepository = new BankAccountRepositoryImpl(userRepository, cardRepository);
        CashMachineRepository cashMachineRepository = new CashMachineRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(bankAccountRepository, cashMachineRepository);


        BankAccountService bankAccountService = new BankAccountServiceImpl(bankAccountRepository);
        TransactionService transactionService = new TransactionServiceImpl(bankAccountRepository, transactionRepository, cashMachineRepository);
        CashMachineService cashMachineService = new CashMachineServiceImpl(cashMachineRepository);
        UserService userService = new UserServiceImpl(userRepository);
        CardService cardService = new CardServiceImpl(cardRepository);
        transactionService.logIn(2222222222222222L, 2222, 1L, 1L, 1L);
        System.out.println(transactionService.findById(1L));
        transactionService.checkBalance(1L, 2L, 1L);
        System.out.println(transactionService.findById(2L));
        transactionService.getMoney(1L, 1L, 3L, new BigDecimal(5600L));
        System.out.println(transactionService.findById(3L));
        transactionService.putMoney(1L, 1L, 4L, new BigDecimal(1000000L));
        System.out.println(transactionService.findById(4L));
    }
}
