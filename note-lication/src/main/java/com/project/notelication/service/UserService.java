package com.project.notelication.service;

import com.project.notelication.model.User;
import com.project.notelication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false); // Email verification required
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public String generateVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        findByEmail(email).ifPresent(user -> {
            user.setVerificationCode(code);
            user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(10)); // 10분 후 만료
            userRepository.save(user);
        });
        return code;
    }

    public boolean verifyEmail(String email, String code) {
        Optional<User> userOptional = findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getVerificationCode() != null && user.getVerificationCode().equals(code) &&
                user.getVerificationCodeExpiry().isAfter(LocalDateTime.now())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiry(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
