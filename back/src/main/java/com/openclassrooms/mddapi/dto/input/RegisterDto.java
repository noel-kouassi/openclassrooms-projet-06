package com.openclassrooms.mddapi.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "Email should not be null or empty")
    @Email(message = "Email input should be an email format")
    private String email;

    @NotBlank(message = "Name should not be null or empty")
    private String name;

    @NotBlank(message = "Password should not be null or empty")
    private String password;
}
