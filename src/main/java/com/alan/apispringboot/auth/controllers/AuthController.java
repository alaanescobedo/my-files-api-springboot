package com.alan.apispringboot.auth.controllers;

import com.alan.apispringboot.auth.dtos.AuthUserDTO;
import com.alan.apispringboot.auth.dtos.RegisterDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.auth.services.AuthService;
import com.alan.apispringboot.security.CurrentUser;
import com.alan.apispringboot.users.entities.User;
import com.alan.apispringboot.users.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private UsersService usersService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDto) {
        logger.info("Registering user: " + registerDto.toString());

        try {
            usersService.registerUser(registerDto);
            return new ResponseEntity<>("Registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating user: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthUserDTO authUserDTO, HttpServletResponse response) {
        logger.info("Login user: " + authUserDTO.toString());

        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authUserDTO.getUsername(), authUserDTO.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Cookie atCookie = authService.getCookieWithAccessToken(authentication);
            Cookie rtCookie = authService.getCookieWithRefreshToken(authentication);
            usersService.saveRefreshToken(rtCookie.getValue(), authUserDTO.getUsername());

            response.addCookie(atCookie);
            response.addCookie(rtCookie);
            return new ResponseEntity<>("Logged in successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error login user: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> authenticate() {
        logger.info("Authenticating user");
        try {
            CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = usersService.getUserByUsername(currentUser.getUsername());
            UserDTO userDTO = usersService.mapUserToUserDTO(user);
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error authenticating user: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        logger.info("Logging out user");
        try {
            SecurityContextHolder.clearContext();
            List<Cookie> cookies = authService.getCookiesForLogout();
            cookies.forEach(response::addCookie);
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error logging out user: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletResponse response) {
        logger.info("Refreshing token");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Cookie atCookie = authService.getCookieWithAccessToken(authentication);
            response.addCookie(atCookie);
            return new ResponseEntity<>("Refreshed successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error refreshing token: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
