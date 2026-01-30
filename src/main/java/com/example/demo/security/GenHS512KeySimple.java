package com.example.demo.security;

import java.security.SecureRandom;
import java.util.Base64;

public class GenHS512KeySimple {
    public static void main(String[] args) {
        // Generate 64 random bytes for HS512
        byte[] keyBytes = new byte[64];
        new SecureRandom().nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        
        System.out.println("=== JWT HS512 Secret Key (SecureRandom) ===");
        System.out.println("Base64 Encoded Key:");
        System.out.println(base64Key);
        System.out.println("\nKey Length: " + keyBytes.length + " bytes ✓");
        System.out.println("\n=== Copy to application.properties ===");
        System.out.println("jwt.secret=" + base64Key);
    }
}
