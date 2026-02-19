package com.skillpass.backend;

import com.skillpass.backend.entity.*;
import com.skillpass.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestUser {

    @Bean
    public CommandLineRunner testerUser(
            UserRepository userRepository,
            TestRepository testRepository,
            TestResultRepository resultRepository) {

        return args -> {
            System.out.println("TEST GESTION DES UTILISATEURS");


            // Test 1 : creer un utilisateur

            System.out.println("\n Création d'un utilisateur...");

            User user = new User();
            user.setEmail("jean.dupont@email.com");
            user.setNom("Dupont");
            user.setPrenom("Jean");
            user.setPassword("password123");
            user.setRole(User.Role.USER);

            User savedUser = userRepository.save(user);
            System.out.println(" Utilisateur créé avec ID: " + savedUser.getId());
            System.out.println("   Email: " + savedUser.getEmail());
            System.out.println("   Rôle: " + savedUser.getRole());

           // test 2 : creation d'un admin
            System.out.println("\n Création d'un admin...");

            User admin = new User();
            admin.setEmail("admin@skillpass.com");
            admin.setNom("Admin");
            admin.setPrenom("Super");
            admin.setPassword("admin123");
            admin.setRole(User.Role.ADMIN);

            User savedAdmin = userRepository.save(admin);
            System.out.println(" Admin créé avec ID: " + savedAdmin.getId());

          // test 3 : recherche par mail
            System.out.println("\n Recherche par email...");

            var foundUser = userRepository.findByEmail("jean.dupont@email.com");
            if (foundUser.isPresent()) {
                System.out.println(" Utilisateur trouvé: " + foundUser.get().getNom());
            }

           // test 4 : nouveau resultat de test
            System.out.println("\n Création d'un résultat de test...");


            var tests = testRepository.findAll();
            if (!tests.isEmpty()) {
                Test test = tests.get(0);

                TestResult result = new TestResult();
                result.setUser(savedUser);
                result.setTest(test);
                result.setScore(15);
                result.setTotalQuestions(20);
                result.setCorrectAnswers(15);
                result.setTimeSpentSeconds(600);

                TestResult savedResult = resultRepository.save(result);
                System.out.println(" Résultat sauvegardé: " + savedResult.getScore() + "/" + savedResult.getTotalQuestions());
                System.out.println("   Pourcentage: " + savedResult.getPercentage() + "%");
                System.out.println("   Note /20: " + savedResult.getScoreOnTwenty());
            }

         //stats
            System.out.println("Statistiques...");

            long totalUsers = userRepository.count();
            long adminCount = userRepository.countByRole(User.Role.ADMIN);
            long userCount = userRepository.countByRole(User.Role.USER);

            System.out.println(" Total utilisateurs: " + totalUsers);
            System.out.println("   - Admins: " + adminCount);
            System.out.println("   - Users: " + userCount);


            System.out.println("\n🎉 GESTION UTILISATEURS TESTÉE AVEC SUCCÈS !");

        };
    }
}