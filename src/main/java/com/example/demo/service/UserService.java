package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepository userRepository,
                       EmailService emailService,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    /* ================= SIGNUP ================= */

    public String signup(SignupRequest req) {

        if (userRepository.existsByEmailIgnoreCase(req.getEmail()))
            throw new RuntimeException("Email already exists");

        if (!req.getPassword().equals(req.getConfirmPassword()))
            throw new RuntimeException("Passwords do not match");

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        userRepository.save(user);

        sendAndSaveOtp(user);
        return "OTP sent to " + user.getEmail();
    }

    /* ================= OTP VERIFY ================= */

    public String verifyOtp(OtpRequest req) {

        User user = userRepository.findByEmailIgnoreCase(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!req.getOtp().equals(user.getResetOtp()))
            throw new RuntimeException("Invalid OTP");

        if (user.getResetOtpExpiry().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        user.setEmailVerified(true);
        user.setResetOtp(null);
        user.setResetOtpExpiry(null);
        userRepository.save(user);

        return "Email verified successfully";
    }

    /* ================= LOGIN ================= */

    public JwtResponse login(LoginRequest req) {

        User user = userRepository.findByEmailIgnoreCase(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.isAccountNonLocked())
            throw new RuntimeException("Account locked");

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= 5)
                user.setAccountNonLocked(false);
            userRepository.save(user);
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEmailVerified())
            throw new RuntimeException("Verify your email first");

        user.setFailedLoginAttempts(0);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        JwtResponse res = new JwtResponse();
        res.setAccessToken(jwtService.generateAccessToken(user));
        res.setRefreshToken(jwtService.generateRefreshToken(user));
        res.setUser(mapToDto(user));
        return res;
    }

    /* ================= FORGOT PASSWORD ================= */

    public String forgotPassword(String email) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        sendAndSaveOtp(user);
        return "OTP sent to your email";
    }

    /* ================= RESET PASSWORD ================= */

    public String resetPassword(ResetPasswordRequest req) {

        User user = userRepository.findByEmailIgnoreCase(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!req.getOtp().equals(user.getResetOtp()))
            throw new RuntimeException("Invalid OTP");

        if (user.getResetOtpExpiry().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        user.setPassword(encoder.encode(req.getNewPassword()));
        user.setResetOtp(null);
        user.setResetOtpExpiry(null);
        userRepository.save(user);

        return "Password reset successful";
    }

    /* ================= HELPERS ================= */

    private void sendAndSaveOtp(User user) {
        String otp = String.format("%06d", new Random().nextInt(1_000_000));
        user.setResetOtp(otp);
        user.setResetOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        emailService.sendOtp(user.getEmail(), otp);
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId().toString());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
