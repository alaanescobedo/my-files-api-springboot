package com.alan.apispringboot.auth.controllers;

import com.alan.apispringboot.Message;
import com.alan.apispringboot.auth.dtos.AuthUserDTO;
import com.alan.apispringboot.auth.dtos.RegisterUserDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.auth.services.AuthService;
import com.alan.apispringboot.users.services.UserAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private UserAuthService userAuthService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDTO registerDto, BindingResult result) {
        logger.info("Registering user: " + registerDto.getUsername());

        if (result.hasErrors()) {
            // TODO: HANDLE ERRORS
            List<ObjectError> errors = result.getAllErrors();
            String errorMessages = errors.stream().map(e -> e.getDefaultMessage()).reduce("", (a, b) -> a + "\n" + b);
            return new ResponseEntity<>(new Message(errorMessages), HttpStatus.BAD_REQUEST);
        }
        try {
            userAuthService.registerUser(registerDto);
            return new ResponseEntity<Message>(new Message("Registered successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating user: " + e.getMessage());
            return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response, @Valid @RequestBody AuthUserDTO authUserDTO, BindingResult bindingResult) {
        logger.info("Login user: " + authUserDTO.getUsername());
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<Message>(new Message("Invalid data"), HttpStatus.BAD_REQUEST);
        }

        try {
            Authentication authentication = userAuthService.createAuthentication(authUserDTO);
            ResponseCookie atCookie = authService.getCookieWithAccessToken(authentication);
            ResponseCookie rtCookie = authService.getCookieWithRefreshToken(authentication);
            userAuthService.saveRefreshToken(rtCookie.getValue(), authUserDTO.getUsername());

            response.addHeader("Set-Cookie", atCookie.toString());
            response.addHeader("Set-Cookie", rtCookie.toString());
            return new ResponseEntity<Message>(new Message("Logged in successfully"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error login user: " + e.getMessage());
            return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> authenticate() {
        logger.info("Authenticating user");
        try {
            UserDTO userDTO = userAuthService.getCurrentUserDTO();
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error authenticating user: " + e.getMessage());
            return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        logger.info("Logging out user");
        try {
            SecurityContextHolder.clearContext();
            List<ResponseCookie> cookies = authService.getCookiesForLogout();
            cookies.forEach(c -> response.addHeader("Set-Cookie", c.toString()));
            return new ResponseEntity<Message>(new Message("Logged out successfully"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error logging out user: " + e.getMessage());
            return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletResponse response) {
        logger.info("Refreshing token");
        try {
            Authentication authentication = userAuthService.getAuthentication();
            ResponseCookie atCookie = authService.getCookieWithAccessToken(authentication);

            response.addHeader("Set-Cookie", atCookie.toString());
            return new ResponseEntity<Message>(new Message("Refreshed successfully"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error refreshing token: " + e.getMessage());
            return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
