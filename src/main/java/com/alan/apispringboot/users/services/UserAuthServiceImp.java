package com.alan.apispringboot.users.services;

import com.alan.apispringboot.suscriptions.Suscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alan.apispringboot.auth.dtos.AuthUserDTO;
import com.alan.apispringboot.auth.dtos.RegisterUserDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.security.CurrentUser;
import com.alan.apispringboot.suscriptions.SuscriptionsService;
import com.alan.apispringboot.users.entities.User;
import com.alan.apispringboot.users.repositories.UsersRepository;

@Service
public class UserAuthServiceImp implements UserAuthService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImp.class);

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private SuscriptionsService suscriptionsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authManagerBuilder;

    @Autowired
    public UserAuthServiceImp(AuthenticationManagerBuilder authManagerBuilder) {
        this.authManagerBuilder = authManagerBuilder;
    }

    @Override
    public void registerUser(RegisterUserDTO registerDto) {
        logger.info("Registering user {}", registerDto.getUsername());
        try {
            String hashedPassword = passwordEncoder.encode(registerDto.getPassword());
            Boolean UserExists = usersRepository.existsByUsername(registerDto.getUsername());
            Boolean EmailExists = usersRepository.existsByEmail(registerDto.getEmail());
            if (UserExists && EmailExists) {
                throw new Exception("Username and email already exists");
            } else if (UserExists) {
                throw new Exception("Username already exists");
            } else if (EmailExists) {
                throw new Exception("Email already exists");
            }

            User user = User.builder()
                    .username(registerDto.getUsername())
                    .email(registerDto.getEmail())
                    .password(hashedPassword)
                    .address(registerDto.getAddress())
                    .avatar(null)
                    .build();
            user.getRoles().add(rolesService.getDefaultRole());

            User userCreated = usersRepository.save(user);

            suscriptionsService.createBasicSuscription(userCreated);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user " + e.getMessage());
        }
    }

    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        logger.info("Saving refresh token for user {}", username);
        try {
            String hashedRefreshToken = passwordEncoder.encode(refreshToken);
            User user = usersService.getUserByUsername(username);
            user.setRefreshToken(hashedRefreshToken);
            usersRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving refresh token " + e.getMessage());
        }
    }

    @Override
    public Authentication createAuthentication(AuthUserDTO authUserDTO) {
        logger.info("Creating authentication for user {}", authUserDTO.getUsername());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authUserDTO.getUsername(),
                authUserDTO.getPassword());
        Authentication authentication = authManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public Authentication getAuthentication() {
        logger.info("Getting authentication");
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserDTO getCurrentUserDTO() {
        logger.info("Getting current userDTO");
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = usersService.getUserByUsername(currentUser.getUsername());
        return usersService.mapUserToUserDTO(user);
    }

    @Override
    public User getCurrentUser() {
        logger.info("Getting current user");
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usersService.getUserByUsername(currentUser.getUsername());
    }
}
