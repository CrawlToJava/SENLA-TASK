package entity;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class BankAccount implements Serializable {

    private Long id;

    private BigDecimal amountOfMoney;

    private Card card;

    private User user;

    private BankAccountStatus bankAccountStatus;
}
