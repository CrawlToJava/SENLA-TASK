package application.entity;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User implements Serializable {
    private Long id;

    private String firstName;

    private String lastName;

    private UserStatus userStatus;
}
