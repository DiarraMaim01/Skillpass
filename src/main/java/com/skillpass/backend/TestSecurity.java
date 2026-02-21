package com.skillpass.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class TestSecurity {

    @Bean
    public CommandLineRunner testerSecurity(PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println(" TEST SPRING SECURITY");


            // Test BCrypt
            String motDePasse = "monMotDePasse123";
            String hash = passwordEncoder.encode(motDePasse);

            System.out.println("\n1 Test BCrypt:");
            System.out.println("   Mot de passe: " + motDePasse);
            System.out.println("   Hash: " + hash);

            boolean match = passwordEncoder.matches(motDePasse, hash);
            System.out.println("   Vérification: " + (match ? "✅ OK" : "❌ Échec"));


            boolean wrongMatch = passwordEncoder.matches("mauvaisMotDePasse", hash);
            System.out.println("   Mauvais mot de passe: " + (wrongMatch ? "❌" : "✅ OK (rejeté)"));

            System.out.println("\nConfiguration Spring Security OK !");


        };
    }
}