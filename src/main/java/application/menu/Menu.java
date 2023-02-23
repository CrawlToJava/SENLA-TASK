package application.menu;

import application.entity.BankAccount;
import application.exception.NoDataFoundException;
import application.service.BankAccountService;
import application.service.CardService;
import application.service.TransactionService;
import application.valid.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class Menu {

    private final TransactionService transactionService;

    private final BankAccountService bankAccountService;

    private final CardService cardService;

    @Autowired
    public Menu(TransactionService transactionService, BankAccountService bankAccountService, CardService cardService) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
        this.cardService = cardService;
    }

    public void createMenu(Long bankAccountId, Long cashMachineId) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean condition = true;
            while (condition) {
                System.out.println("Enter transaction number:\n1. Log in\n2. Check balance\n3. Withdraw money from the account\n4. Deposit money into the account\n5. Log out");
                switch (scanner.nextInt()) {
                    case 1 -> {
                        int attempt = 0;
                        while (true) {
                            BankAccount bankAccount = bankAccountService
                                    .findById(bankAccountId)
                                    .orElseThrow(() -> new NoDataFoundException("Account doesn't exist"));
                            Valid.isAccountNotAuthorized(bankAccount, bankAccountService);
                            Valid.isCardAvailable(bankAccount);
                            System.out.println("Enter card number: ");
                            Long cardNumber = scanner.nextLong();
                            Valid.isCardNumberExist(cardNumber, bankAccount);
                            System.out.println("Enter PIN code: ");
                            Integer pinCode = scanner.nextInt();
                            attempt++;
                            cardService.blockCardIfAttemptsMoreThenThree(attempt, pinCode, bankAccountId);
                            if (pinCode.equals(bankAccount.getCard().getPinCode())) {
                                transactionService.logIn(cardNumber
                                        , pinCode
                                        , bankAccountId
                                        , cashMachineId, (long) (Math.random() * 1000000000));
                                break;
                            }
                        }
                    }
                    case 2 ->
                            transactionService.checkBalance(bankAccountId, (long) (Math.random() * 1000000000), cashMachineId);
                    case 3 -> {
                        Valid.isAccountAuthorised(bankAccountService
                                .findById(bankAccountId)
                                .orElseThrow(() -> new NoDataFoundException("Account doesn't exist")));
                        System.out.println("Enter the amount of money: ");
                        BigDecimal howMuchMoneyGet = new BigDecimal(scanner.nextLong());
                        transactionService.getMoney(bankAccountId
                                , cashMachineId
                                , (long) (Math.random() * 1000000000)
                                , howMuchMoneyGet);
                    }
                    case 4 -> {
                        BankAccount bankAccount = bankAccountService
                                .findById(bankAccountId)
                                .orElseThrow(() -> new NoDataFoundException("Account doesn't exist"));
                        Valid.isAccountAuthorised(bankAccount);
                        System.out.println("Enter the amount of money: ");
                        BigDecimal howMuchMoneyPut = new BigDecimal(scanner.nextLong());
                        transactionService.putMoney(bankAccountId
                                , cashMachineId
                                , (long) (Math.random() * 1000000000)
                                , howMuchMoneyPut);
                    }
                    case 5 -> {
                        transactionService.logOut(bankAccountId, cashMachineId, (long) (Math.random() * 1000000000));
                        condition = false;
                    }
                    default -> System.out.println("Transaction doesn't exist");
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
