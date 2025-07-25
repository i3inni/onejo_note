package com.project.notelication.controller;

import com.project.notelication.model.User;
import com.project.notelication.service.EmailService;
import com.project.notelication.service.UserService;
import com.project.notelication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is already taken!"));
        }
        if (userService.findByNickname(user.getNickname()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Nickname is already taken!"));
        }
        userService.registerUser(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully. Please verify your email."));
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required."));
        }
        if (userService.findByEmail(email).isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email not found."));
        }

        String code = userService.generateVerificationCode(email);
        emailService.sendVerificationCode(email, code);
        return ResponseEntity.ok(Map.of("message", "Verification code sent to your email."));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (userService.verifyEmail(email, code)) {
            return ResponseEntity.ok(Map.of("message", "Email verified successfully."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid email or verification code."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User user) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid email or password."));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of("jwt", jwt));
    }
}
