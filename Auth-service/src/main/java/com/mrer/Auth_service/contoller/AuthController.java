package com.mrer.Auth_service.contoller;

import com.mrer.Auth_service.model.User;
import com.mrer.Auth_service.service.AuthService;
import com.mrer.Auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(
                Map.of("token", service.register(req.get("email"), req.get("password")))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(
                Map.of("token", service.login(req.get("email"), req.get("password")))
        );
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
