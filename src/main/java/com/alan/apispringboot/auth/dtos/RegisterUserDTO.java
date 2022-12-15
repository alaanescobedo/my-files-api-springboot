package com.alan.apispringboot.auth.dtos;

import com.alan.apispringboot.users.entities.Address;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterUserDTO {

    @NotBlank(message = "Username is required")
    @Length(min = 7, max = 30, message = "Username must be between 7 and 30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Length(min = 7,message = "Password must be at least 7 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Length(min = 7, max = 100, message = "Email must be between 7 and 100 characters")
    private String email;

    private Address address;
}
