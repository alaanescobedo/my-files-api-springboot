package com.alan.apispringboot.auth.dtos;

import com.alan.apispringboot.users.entities.Address;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterDTO {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Email is required")
    private String email;

    private Address address;
}
