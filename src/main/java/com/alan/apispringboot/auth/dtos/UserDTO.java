package com.alan.apispringboot.auth.dtos;

import com.alan.apispringboot.files.FilePublic;
import com.alan.apispringboot.users.entities.Address;
import com.alan.apispringboot.users.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDTO {

    @NotBlank
    private Long id;

    @NotBlank
    @Length(min = 7, max = 30)
    private String username;

    @NotBlank
    @Length(min = 7, max = 100)
    private String email;

    private FilePublic avatar;

    @NotBlank
    private Set<Role> roles;
    private Address address;
}
