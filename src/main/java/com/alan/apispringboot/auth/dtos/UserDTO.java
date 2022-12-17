package com.alan.apispringboot.auth.dtos;

import com.alan.apispringboot.files.entities.FilePublic;
import com.alan.apispringboot.subscription.Subscription;
import com.alan.apispringboot.users.entities.Address;
import com.alan.apispringboot.users.entities.Role;
import com.alan.apispringboot.users.entities.UserAvatar;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private UserAvatar avatar;

    @JsonIgnore
    private Set<FilePublic> filesPublic;

    @NotBlank
    private Set<Role> roles;
    private Address address;

    private Subscription subscription;
}
