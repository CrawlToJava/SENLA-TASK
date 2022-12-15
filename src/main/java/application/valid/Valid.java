package application.valid;

import application.entity.*;
import application.exception.NotAvailableException;
import application.service.BankAccountService;

import java.math.BigDecimal;

public class Valid {

    public static boolean isCardNumberCorrect(Long cardNumber, Card card) {
        if (cardNumber.equals(card.getCardNumber())) {
            return true;
        }
        throw new NotAvailableException("Номер карты введен не правильно или не существует");
    }

    public static boolean isPinCodeCorrect(Integer pinCode, Card card) {
        if (pinCode.equals(card.getPinCode())) {
            return true;
        }
        throw new NotAvailableException("Пинкод введен неверно");
    }

    public static boolean isAccountAuthorised(BankAccount bankAccount) {
        if (bankAccount.getBankAccountStatus().equals(BankAccountStatus.AUTHORIZED)) {
            return true;
        }
        throw new NotAvailableException("Вы не авторизировались в аккаунте");
    }

    public static boolean isEnoughMoneyOnTheBalance(BankAccount bankAccount, BigDecimal howMuchMoneyWithdraw, BankAccountService bankAccountService) {
        if (bankAccount
                .getAmountOfMoney()
                .compareTo(howMuchMoneyWithdraw) >= 0) {
            return true;
        }
        changeBankAccountStatusIfConditionFalse(bankAccount, bankAccountService);
        throw new NotAvailableException("У вас недосаточно средств на балансе");
    }

    public static boolean isWithdrawMoneyNotGreaterThanCashMachineLimit(BigDecimal howMuchMoneyWithdraw, CashMachine cashMachine, BankAccount bankAccount, BankAccountService bankAccountService) {
        if (cashMachine.getCashMachineMoneyLimit().compareTo(howMuchMoneyWithdraw) >= 0
                && !howMuchMoneyWithdraw.equals(new BigDecimal(0))
                && howMuchMoneyWithdraw.compareTo(new BigDecimal(-1)) > 0) {
            return true;
        }
        changeBankAccountStatusIfConditionFalse(bankAccount, bankAccountService);
        throw new NotAvailableException("Вы не можете снять со счета данную сумму");
    }

    public static boolean isCashMachineOpen(CashMachine cashMachine) {
        if (cashMachine.getCashMachineStatus().equals(CashMachineStatus.OPEN)) {
            return true;
        }
        throw new NotAvailableException("Банкомат в данный момент не работает");
    }

    public static boolean isPutMoneyAvailable(BigDecimal howMuchMoneyPutOnTheBalance, BankAccount bankAccount, BankAccountService bankAccountService) {
        if (howMuchMoneyPutOnTheBalance.compareTo(new BigDecimal(1000000L)) <= 0
                && !howMuchMoneyPutOnTheBalance.equals(new BigDecimal(0))
                && howMuchMoneyPutOnTheBalance.compareTo(new BigDecimal(-1)) > 0) {
            return true;
        }
        changeBankAccountStatusIfConditionFalse(bankAccount, bankAccountService);
        throw new NotAvailableException("Вы не можете пополнить баланс на данную сумму");
    }

    public static boolean isCardAvailable(BankAccount bankAccount) {
        if (bankAccount.getCard().getCardStatus().equals(CardStatus.AVAILABLE)) {
            return true;
        }
        throw new NotAvailableException("Ваша карта заблокирована!");
    }

    public static boolean isUserFriendly(BankAccount bankAccount) {
        if (bankAccount.getUser().getUserStatus().equals(UserStatus.FRIENDLY)) {
            return true;
        }
        throw new NotAvailableException("Вы занесены в черный список");
    }

    public static boolean isAccountNotAuthorized(BankAccount bankAccount, BankAccountService bankAccountService) {
        if (bankAccount.getBankAccountStatus().equals(BankAccountStatus.NOT_AUTHORIZED)) {
            return true;
        }
        changeBankAccountStatusIfConditionFalse(bankAccount, bankAccountService);
        throw new NotAvailableException("Вы уже авторизовались в аккаунте");
    }

    public static void isCardNumberExist(Long cardNumber, BankAccount bankAccount) {
        if (!cardNumber.equals(bankAccount.getCard().getCardNumber())) {
            throw new NotAvailableException("Карты с таким номером не существует");
        }
    }

    private static void changeBankAccountStatusIfConditionFalse(BankAccount bankAccount, BankAccountService bankAccountService) {
        bankAccountService.update(new BankAccount(bankAccount.getId()
                , bankAccount.getAmountOfMoney()
                , bankAccount.getCard()
                , bankAccount.getUser()
                , BankAccountStatus.NOT_AUTHORIZED));
    }
}
