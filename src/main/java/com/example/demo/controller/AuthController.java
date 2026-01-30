package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication APIs")

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://siva17061997.github.io")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /* ================= SIGNUP ================= */

    @Operation(summary = "User Registration")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(userService.signup(request));
    }

    /* ================= VERIFY OTP ================= */

    @Operation(summary = "Verify Email OTP")
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(userService.verifyOtp(request));
    }

    /* ================= LOGIN ================= */

    @Operation(summary = "User Login")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    /* ================= FORGOT PASSWORD ================= */

    @Operation(summary = "Forgot Password (Send OTP)")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(userService.forgotPassword(request.getEmail()));
    }

    /* ================= RESET PASSWORD ================= */

    @Operation(summary = "Reset Password using OTP")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(userService.resetPassword(request));
    }

    /* ================= PROFILE ================= */

    @Operation(summary = "Get Current User Profile")
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(new UserDto());
    }
}
