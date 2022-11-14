package entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Card implements Serializable {

    private Long id;

    private Integer pinCode;

    private Long cardNumber;

    private CardStatus cardStatus;
}
