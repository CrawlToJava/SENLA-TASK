package entity;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CashMachine {
    private Long id;

    private String address;

    private BigDecimal CashMachineMoneyLimit;

    private CashMachineStatus cashMachineStatus;
}
