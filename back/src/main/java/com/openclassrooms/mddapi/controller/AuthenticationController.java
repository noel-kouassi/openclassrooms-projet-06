package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.input.LoginDto;
import com.openclassrooms.mddapi.dto.input.RegisterDto;
import com.openclassrooms.mddapi.dto.response.JwtTokenResponse;
import com.openclassrooms.mddapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param registerDto input data which contains the user information for his creation
     * @return Jwt as token response
     */
    @PostMapping("/register")
    public ResponseEntity<JwtTokenResponse> registerUser(@Valid @RequestBody RegisterDto registerDto) {

        userService.checkUser(registerDto);
        userService.saveUser(registerDto);
        LoginDto loginDto = new LoginDto(registerDto.getEmail(), registerDto.getPassword());
        String userToken = userService.login(loginDto);
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse(userToken);

        return new ResponseEntity<>(jwtTokenResponse, HttpStatus.OK);
    }

    /**
     * @param loginDto input data which contains the user credentials for his connection
     * @return Jwt as token response
     */
    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> loginUser(@Valid @RequestBody LoginDto loginDto) {
        String userToken = userService.login(loginDto);
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse(userToken);
        jwtTokenResponse.setToken(userToken);
        return new ResponseEntity<>(jwtTokenResponse, HttpStatus.OK);
    }
}
