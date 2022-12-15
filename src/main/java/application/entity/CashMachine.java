package application.entity;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CashMachine implements Serializable {
    private Long id;

    private String address;

    private BigDecimal cashMachineMoneyLimit;

    private CashMachineStatus cashMachineStatus;
}
