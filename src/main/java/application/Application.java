package application;

import menu.Menu;
import repository.*;
import repository.impl.*;
import service.BankAccountService;
import service.CardService;
import service.TransactionService;
import service.impl.BankAccountServiceImpl;
import service.impl.CardServiceImpl;
import service.impl.TransactionServiceImpl;

public class Application {
    public static void main(String[] args) {
        CardRepository cardRepository = new CardRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        BankAccountRepository bankAccountRepository = new BankAccountRepositoryImpl(userRepository, cardRepository);
        CashMachineRepository cashMachineRepository = new CashMachineRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(bankAccountRepository, cashMachineRepository);
        TransactionService transactionService = new TransactionServiceImpl(bankAccountRepository, transactionRepository, cashMachineRepository);
        BankAccountService bankAccountService = new BankAccountServiceImpl(bankAccountRepository);
        CardService cardService = new CardServiceImpl(cardRepository, bankAccountRepository);
        Menu menu = new Menu(transactionService, bankAccountService, cardService);
        menu.createMenu(1L, 1L);
    }
}
