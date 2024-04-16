package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.input.RegisterDto;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param registerDto input data which contains the user information for modification
     * @param login       the email or name of the user to be updated
     * @return Jwt as token response
     */
    @PutMapping("/update/{login}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> loginUser(@RequestBody RegisterDto registerDto, @PathVariable(value = "login") String login) {
        userService.updateUser(registerDto, login);
        return new ResponseEntity<>("User updated successfully !", HttpStatus.OK);
    }
}
