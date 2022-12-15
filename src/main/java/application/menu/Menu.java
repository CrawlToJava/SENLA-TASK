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

@Component("menu")
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
                System.out.println("Выбирите номер операции:\n1. Авторизоваться\n2. Проверить баланс\n3. Снять деньги со счета\n4. Пополнить счет\n5. Выйти");
                switch (scanner.nextInt()) {
                    case 1 -> {
                        int attempt = 0;
                        while (true) {
                            BankAccount bankAccount = bankAccountService
                                    .findById(bankAccountId)
                                    .orElseThrow(() -> new NoDataFoundException("Аккаунта с таким id не существует"));
                            Valid.isAccountNotAuthorized(bankAccount, bankAccountService);
                            Valid.isCardAvailable(bankAccount);
                            System.out.println("Введите номер карты: ");
                            Long cardNumber = scanner.nextLong();
                            Valid.isCardNumberExist(cardNumber, bankAccount);
                            System.out.println("Введите пинкод: ");
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
                                .orElseThrow(() -> new NoDataFoundException("Аккаунта с таким id не существует")));
                        System.out.println("Введите сумму: ");
                        BigDecimal howMuchMoneyGet = new BigDecimal(scanner.nextLong());
                        transactionService.getMoney(bankAccountId
                                , cashMachineId
                                , (long) (Math.random() * 1000000000)
                                , howMuchMoneyGet);
                    }
                    case 4 -> {
                        BankAccount bankAccount = bankAccountService
                                .findById(bankAccountId)
                                .orElseThrow(() -> new NoDataFoundException("Аккаунта с таким id не существует"));
                        Valid.isAccountAuthorised(bankAccount);
                        System.out.println("Введите сумму: ");
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
                    default -> System.out.println("Такой операции не существует");
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
