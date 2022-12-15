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
        throw new NotAvailableException("Card number is incorrect");
    }

    public static boolean isPinCodeCorrect(Integer pinCode, Card card) {
        if (pinCode.equals(card.getPinCode())) {
            return true;
        }
        throw new NotAvailableException("PIN code is incorrect");
    }

    public static boolean isAccountAuthorised(BankAccount bankAccount) {
        if (bankAccount.getBankAccountStatus().equals(BankAccountStatus.AUTHORIZED)) {
            return true;
        }
        throw new NotAvailableException("You are not logged in");
    }

    public static boolean isEnoughMoneyOnTheBalance(BankAccount bankAccount, BigDecimal howMuchMoneyWithdraw, BankAccountService bankAccountService) {
        if (bankAccount
                .getAmountOfMoney()
                .compareTo(howMuchMoneyWithdraw) >= 0) {
            return true;
        }
        changeBankAccountStatusIfConditionFalse(bankAccount, bankAccountService);
        throw new NotAvailableException("You don`t have enough money");
    }

    public static boolean isWithdrawMoneyNotGreaterThanCashMachineLimit(BigDecimal howMuchMoneyWithdraw, CashMachine cashMachine, BankAccount bankAccount, BankAccountService bankAccountService) {
        if (cashMachine.getCashMachineMoneyLimit().compareTo(howMuchMoneyWithdraw) >= 0
                && !howMuchMoneyWithdraw.equals(new BigDecimal(0))
                && howMuchMoneyWithdraw.compareTo(new BigDecimal(-1)) > 0) {
            return true;
        }
        changeBankAccountStatusIfConditionFalse(bankAccount, bankAccountService);
        throw new NotAvailableException("You cannot withdraw this amount of money from the account");
    }

    public static boolean isCashMachineOpen(CashMachine cashMachine) {
        if (cashMachine.getCashMachineStatus().equals(CashMachineStatus.OPEN)) {
            return true;
        }
        throw new NotAvailableException("Cash machine doesn't work");
    }

    public static boolean isPutMoneyAvailable(BigDecimal howMuchMoneyPutOnTheBalance, BankAccount bankAccount, BankAccountService bankAccountService) {
        if (howMuchMoneyPutOnTheBalance.compareTo(new BigDecimal(1000000L)) <= 0
                && !howMuchMoneyPutOnTheBalance.equals(new BigDecimal(0))
                && howMuchMoneyPutOnTheBalance.compareTo(new BigDecimal(-1)) > 0) {
            return true;
        }
        changeBankAccountStatusIfConditionFalse(bankAccount, bankAccountService);
        throw new NotAvailableException("You cannot deposit this amount of money from the account");
    }

    public static boolean isCardAvailable(BankAccount bankAccount) {
        if (bankAccount.getCard().getCardStatus().equals(CardStatus.AVAILABLE)) {
            return true;
        }
        throw new NotAvailableException("Your card is blocked!");
    }

    public static boolean isUserFriendly(BankAccount bankAccount) {
        if (bankAccount.getUser().getUserStatus().equals(UserStatus.FRIENDLY)) {
            return true;
        }
        throw new NotAvailableException("You are blacklisted");
    }

    public static boolean isAccountNotAuthorized(BankAccount bankAccount, BankAccountService bankAccountService) {
        if (bankAccount.getBankAccountStatus().equals(BankAccountStatus.NOT_AUTHORIZED)) {
            return true;
        }
        changeBankAccountStatusIfConditionFalse(bankAccount, bankAccountService);
        throw new NotAvailableException("You are not logged in");
    }

    public static void isCardNumberExist(Long cardNumber, BankAccount bankAccount) {
        if (!cardNumber.equals(bankAccount.getCard().getCardNumber())) {
            throw new NotAvailableException("Card doesn't exist");
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
