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
        transactionService.checkBalance(1L, 5L, 1L);
        transactionService.getMoney(1L, 1L, 6L, new BigDecimal(32222L));
        transactionService.checkBalance(1L, 7L, 1L);
    }
}
