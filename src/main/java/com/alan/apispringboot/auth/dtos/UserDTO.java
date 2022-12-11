package com.alan.apispringboot.auth.dtos;

import com.alan.apispringboot.users.entities.Address;
import com.alan.apispringboot.users.entities.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class UserDTO {

    @NotBlank
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private Set<Role> roles;
    private Address address;
}
