package com.alan.apispringboot.auth.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class AuthUserDTO {

    @Length(min = 7, max = 30, message = "Username must be between 7 and 30 characters")
    @NotBlank(message = "Username is required")
    private String username;

    @Length(min = 7,message = "Password must be at least 7 characters")
    @NotBlank(message = "Password is required")
    private String password;

}
