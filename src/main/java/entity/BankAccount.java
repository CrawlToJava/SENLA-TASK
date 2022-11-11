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

    private User user;

    private BigDecimal amountOfMoney;

    private Card card;

    private BankAccountStatus bankAccountStatus;
}
