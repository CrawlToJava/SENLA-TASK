package menu;

import exception.NoDataFoundException;
import lombok.AllArgsConstructor;
import service.BankAccountService;
import service.TransactionService;
import valid.Valid;

import java.math.BigDecimal;
import java.util.Scanner;

@AllArgsConstructor
public class Menu {

    private TransactionService transactionService;

    private BankAccountService bankAccountService;

    public void menu(Long bankAccountId, Long cashMachineId) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean condition = true;
            while (condition) {
                System.out.println("Выбирите номер операции:\n1. Авторизоваться\n2. Проверить баланс\n3. Снять деньги со счета\n4. Пополнить счет\n5. Выйти");
                switch (scanner.nextInt()) {
                    case 1 -> {
                        Valid.isAccountNotAuthorized(bankAccountService.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Аккаунта с таким id не существует")));
                        System.out.println("Введите номер карты: ");
                        Long cardNumber = scanner.nextLong();
                        System.out.println("Введите пинкод: ");
                        Integer pinCode = scanner.nextInt();
                        transactionService.logIn(cardNumber, pinCode, bankAccountId, cashMachineId, (long) (Math.random() * 1000000000));
                    }
                    case 2 ->
                            transactionService.checkBalance(bankAccountId, (long) (Math.random() * 1000000000), cashMachineId);
                    case 3 -> {
                        Valid.isAccountAuthorised(bankAccountService.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Аккаунта с таким id не существует")));
                        System.out.println("Введите сумму: ");
                        BigDecimal howMuchMoneyGet = new BigDecimal(scanner.nextLong());
                        transactionService.getMoney(bankAccountId, cashMachineId, (long) (Math.random() * 1000000000), howMuchMoneyGet);
                    }
                    case 4 -> {
                        Valid.isAccountAuthorised(bankAccountService.findById(bankAccountId).orElseThrow(() -> new NoDataFoundException("Аккаунта с таким id не существует")));
                        System.out.println("Введите сумму: ");
                        BigDecimal howMuchMoneyPut = new BigDecimal(scanner.nextLong());
                        transactionService.putMoney(bankAccountId, cashMachineId, (long) (Math.random() * 1000000000), howMuchMoneyPut);
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
