package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.input.LoginDto;
import com.openclassrooms.mddapi.dto.input.RegisterDto;
import com.openclassrooms.mddapi.dto.response.UserDto;
import com.openclassrooms.mddapi.entity.Role;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.GlobalException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.repository.RoleRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtTokenProvider;
import com.openclassrooms.mddapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.openclassrooms.mddapi.util.DateFormatter.formatDate;
import static com.openclassrooms.mddapi.util.PasswordValidator.isValid;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = modelMapper;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmailOrName(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public UserDto getUserFromToken(HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.getTokenFromRequest(httpServletRequest);
        if (token != null && !token.equalsIgnoreCase("jwt")) {
            if (jwtTokenProvider.validateToken(token)) {
                String login = jwtTokenProvider.getUsername(token);
                return getUserByLogin(login);
            }
        }
        return null;
    }

    @Override
    public UserDto getUserByLogin(String login) {
        User user = userRepository.findByEmailOrName(login, login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with email or name: %s", login)));
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setCreatedAt(formatDate(user.getCreatedAt()));
        userDto.setUpdatedAt(formatDate(user.getUpdatedAt()));
        return userDto;
    }

    @Override
    public void saveUser(RegisterDto registerDto) {

        if (!isValid(registerDto.getPassword())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Password does not respect the security constraints");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, String.format("Email %s is already in database", registerDto.getEmail()));
        }
        if (userRepository.existsByName(registerDto.getName())) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, String.format("Name %s is already in database", registerDto.getName()));
        }
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role not found in database with name ROLE_USER"));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public User updateUser(RegisterDto registerDto, String emailOrName) {

        User user = userRepository.findByEmailOrName(emailOrName, emailOrName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with email or name: %s", emailOrName)));

        String currentPassword = user.getPassword();
        String oldPassword = registerDto.getOldPassword();
        String newPassword = registerDto.getPassword();

        if (registerDto.getEmail() != null) {
            if (userRepository.existsByEmail(registerDto.getEmail())) {
                throw new GlobalException(HttpStatus.BAD_REQUEST, String.format("New email %s is already in database", registerDto.getEmail()));
            }
            user.setEmail(registerDto.getEmail());
        }
        if (registerDto.getName() != null) {
            if (userRepository.existsByName(registerDto.getName())) {
                throw new GlobalException(HttpStatus.BAD_REQUEST, String.format("New name %s is already in database", registerDto.getName()));
            }
            user.setName(registerDto.getName());
        }
        if (newPassword != null) {
            if (isValid(newPassword)) {
                if (passwordEncoder.matches(oldPassword, currentPassword)) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                } else {
                    throw new GlobalException(HttpStatus.BAD_REQUEST, "New password and current password does not match");
                }
            } else {
                throw new GlobalException(HttpStatus.BAD_REQUEST, "New password does not respect the security constraints");
            }
        }
        return userRepository.save(user);
    }
}
