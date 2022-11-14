package application;

import menu.Menu;
import repository.*;
import repository.impl.*;
import service.*;
import service.impl.*;

import java.math.BigDecimal;

public class Application {
    public static void main(String[] args) {
        CardRepository cardRepository = new CardRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        BankAccountRepository bankAccountRepository = new BankAccountRepositoryImpl(userRepository, cardRepository);
        CashMachineRepository cashMachineRepository = new CashMachineRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(bankAccountRepository, cashMachineRepository);
        TransactionService transactionService = new TransactionServiceImpl(bankAccountRepository, transactionRepository, cashMachineRepository);
        BankAccountService bankAccountService = new BankAccountServiceImpl(bankAccountRepository);
        Menu menu = new Menu(transactionService, bankAccountService);
        menu.menu(1L, 1L);
    }
}
