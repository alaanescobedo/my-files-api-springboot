package com.alan.apispringboot.auth.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthUserDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

}
