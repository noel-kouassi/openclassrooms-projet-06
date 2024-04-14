package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.input.LoginDto;
import com.openclassrooms.mddapi.dto.input.RegisterDto;

public interface UserService {

    void saveUser(RegisterDto registerDto);

    String login(LoginDto loginDto);

    void checkUser(RegisterDto registerDto);
}
