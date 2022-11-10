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

    private String description;

    private TransactionStatus transactionStatus;
}
