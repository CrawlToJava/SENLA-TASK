package entity;

import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User {
    private Long id;

    private String firstName;

    private String lastName;

    private UserStatus userStatus;
}
