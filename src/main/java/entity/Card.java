package entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Card {

    private Long id;

    private Integer pinCode;

    private Long cardNumber;

    private CardStatus cardStatus;
}
