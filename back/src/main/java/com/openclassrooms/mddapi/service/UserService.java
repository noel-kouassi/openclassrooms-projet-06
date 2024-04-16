package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.input.LoginDto;
import com.openclassrooms.mddapi.dto.input.RegisterDto;
import com.openclassrooms.mddapi.dto.response.UserDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    void saveUser(RegisterDto registerDto);

    void updateUser(RegisterDto registerDto, String emailOrName);

    String login(LoginDto loginDto);

    UserDto getUserFromToken(HttpServletRequest httpServletRequest);

    UserDto getUserByLogin(String login);
}
