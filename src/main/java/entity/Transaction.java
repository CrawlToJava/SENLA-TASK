package entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Transaction {
    private Long id;

    private BankAccount bankAccount;

    private CashMachine cashMachine;

    private TransactionStatus transactionStatus;
}
