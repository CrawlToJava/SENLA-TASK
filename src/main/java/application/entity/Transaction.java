package application.entity;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Transaction implements Serializable {
    private Long id;

    private BankAccount bankAccount;

    private CashMachine cashMachine;

    private String description;

    private TransactionStatus transactionStatus;
}
