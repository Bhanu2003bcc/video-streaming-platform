package com.mrer.Auth_service.service;

import com.mrer.Auth_service.model.Role;
import com.mrer.Auth_service.model.User;
import com.mrer.Auth_service.repository.UserRepository;
import com.mrer.Auth_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.mrer.Auth_service.model.Role.USER;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public String register(String email, String password) {
        if (repository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setRole(Role.USER);

        repository.save(user);
        return jwtUtil.generateToken(user);
    }

    public String login(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtil.generateToken(user);
    }
}
