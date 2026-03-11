package com.skillpass.backend.config;

import com.skillpass.backend.entity.*;
import com.skillpass.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            QuestionRepository questionRepository,
            TestRepository testRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.existsByEmail("admin@skillpass.com")) return;

            // ===================== USERS =====================
            User admin = new User();
            admin.setEmail("admin@skillpass.com");
            admin.setNom("Admin");
            admin.setPrenom("SkillPass");
            admin.setPassword(passwordEncoder.encode("Admin1234!"));
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);

            User user1 = new User();
            user1.setEmail("demo@skillpass.com");
            user1.setNom("Dupont");
            user1.setPrenom("Marie");
            user1.setPassword(passwordEncoder.encode("Demo1234!"));
            user1.setRole(User.Role.USER);
            userRepository.save(user1);

            // ===================== QUESTIONS JAVA =====================

            // DÉBUTANT
            Question q1 = new Question();
            q1.setTitre("Qu'est-ce que la JVM ?");
            q1.setContenu("Parmi ces propositions, laquelle décrit correctement la JVM (Java Virtual Machine) ?");
            q1.setCategorie(Question.Categorie.JAVA);
            q1.setNiveau(Question.Niveau.DÉBUTANT);
            q1.setPoints(5);
            q1.createOption("Un éditeur de code Java", false);
            q1.createOption("Une machine virtuelle qui exécute le bytecode Java", true);
            q1.createOption("Un compilateur Java", false);
            q1.createOption("Un framework Java", false);
            questionRepository.save(q1);

            Question q2 = new Question();
            q2.setTitre("Types primitifs en Java");
            q2.setContenu("Lequel de ces types n'est PAS un type primitif en Java ?");
            q2.setCategorie(Question.Categorie.JAVA);
            q2.setNiveau(Question.Niveau.DÉBUTANT);
            q2.setPoints(5);
            q2.createOption("int", false);
            q2.createOption("boolean", false);
            q2.createOption("String", true);
            q2.createOption("char", false);
            questionRepository.save(q2);

            Question q3 = new Question();
            q3.setTitre("Modificateur d'accès public");
            q3.setContenu("Que signifie le modificateur d'accès 'public' en Java ?");
            q3.setCategorie(Question.Categorie.JAVA);
            q3.setNiveau(Question.Niveau.DÉBUTANT);
            q3.setPoints(5);
            q3.createOption("Accessible uniquement dans la même classe", false);
            q3.createOption("Accessible uniquement dans le même package", false);
            q3.createOption("Accessible depuis n'importe quelle classe", true);
            q3.createOption("Accessible uniquement par les sous-classes", false);
            questionRepository.save(q3);

            Question q4 = new Question();
            q4.setTitre("Boucle for-each");
            q4.setContenu("Quelle est la syntaxe correcte d'une boucle for-each en Java pour un tableau int[] nombres ?");
            q4.setCategorie(Question.Categorie.JAVA);
            q4.setNiveau(Question.Niveau.DÉBUTANT);
            q4.setPoints(5);
            q4.createOption("for (int n in nombres)", false);
            q4.createOption("for (int n : nombres)", true);
            q4.createOption("foreach (int n : nombres)", false);
            q4.createOption("for each (int n in nombres)", false);
            questionRepository.save(q4);

            // INTERMÉDIAIRE
            Question q5 = new Question();
            q5.setTitre("Interface vs Classe abstraite");
            q5.setContenu("Quelle est la principale différence entre une interface et une classe abstraite en Java ?");
            q5.setCategorie(Question.Categorie.JAVA);
            q5.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q5.setPoints(10);
            q5.createOption("Une interface peut avoir des constructeurs, pas une classe abstraite", false);
            q5.createOption("Une classe peut implémenter plusieurs interfaces mais n'hériter que d'une classe abstraite", true);
            q5.createOption("Une classe abstraite ne peut pas avoir de méthodes concrètes", false);
            q5.createOption("Il n'y a aucune différence", false);
            questionRepository.save(q5);

            Question q6 = new Question();
            q6.setTitre("Collections Java");
            q6.setContenu("Quelle collection Java garantit l'unicité des éléments et un accès en O(1) ?");
            q6.setCategorie(Question.Categorie.JAVA);
            q6.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q6.setPoints(10);
            q6.createOption("ArrayList", false);
            q6.createOption("LinkedList", false);
            q6.createOption("HashSet", true);
            q6.createOption("TreeMap", false);
            questionRepository.save(q6);

            Question q7 = new Question();
            q7.setTitre("Gestion des exceptions");
            q7.setContenu("Quel bloc est toujours exécuté, qu'une exception soit levée ou non ?");
            q7.setCategorie(Question.Categorie.JAVA);
            q7.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q7.setPoints(10);
            q7.createOption("catch", false);
            q7.createOption("try", false);
            q7.createOption("finally", true);
            q7.createOption("throws", false);
            questionRepository.save(q7);

            // EXPERT
            Question q8 = new Question();
            q8.setTitre("Java Stream API");
            q8.setContenu("Quelle opération Stream est une opération terminale ?");
            q8.setCategorie(Question.Categorie.JAVA);
            q8.setNiveau(Question.Niveau.EXPERT);
            q8.setPoints(15);
            q8.createOption("filter()", false);
            q8.createOption("map()", false);
            q8.createOption("collect()", true);
            q8.createOption("sorted()", false);
            questionRepository.save(q8);

            Question q9 = new Question();
            q9.setTitre("Garbage Collector");
            q9.setContenu("Quel algorithme de GC est conçu pour minimiser les pauses dans les applications Java ?");
            q9.setCategorie(Question.Categorie.JAVA);
            q9.setNiveau(Question.Niveau.EXPERT);
            q9.setPoints(15);
            q9.createOption("Serial GC", false);
            q9.createOption("Parallel GC", false);
            q9.createOption("G1 GC", true);
            q9.createOption("Mark and Sweep", false);
            questionRepository.save(q9);

            // ===================== QUESTIONS SQL =====================

            Question q10 = new Question();
            q10.setTitre("Commande SELECT de base");
            q10.setContenu("Quelle commande SQL permet de récupérer toutes les colonnes d'une table 'users' ?");
            q10.setCategorie(Question.Categorie.SQL);
            q10.setNiveau(Question.Niveau.DÉBUTANT);
            q10.setPoints(5);
            q10.createOption("GET * FROM users", false);
            q10.createOption("SELECT ALL users", false);
            q10.createOption("SELECT * FROM users", true);
            q10.createOption("FETCH * FROM users", false);
            questionRepository.save(q10);

            Question q11 = new Question();
            q11.setTitre("Clause WHERE");
            q11.setContenu("Quelle clause SQL est utilisée pour filtrer les résultats ?");
            q11.setCategorie(Question.Categorie.SQL);
            q11.setNiveau(Question.Niveau.DÉBUTANT);
            q11.setPoints(5);
            q11.createOption("FILTER", false);
            q11.createOption("WHERE", true);
            q11.createOption("HAVING", false);
            q11.createOption("CONDITION", false);
            questionRepository.save(q11);

            Question q12 = new Question();
            q12.setTitre("Clé primaire");
            q12.setContenu("Que garantit une clé primaire (PRIMARY KEY) dans une table SQL ?");
            q12.setCategorie(Question.Categorie.SQL);
            q12.setNiveau(Question.Niveau.DÉBUTANT);
            q12.setPoints(5);
            q12.createOption("Que la colonne contient uniquement des nombres", false);
            q12.createOption("Que chaque valeur est unique et non nulle", true);
            q12.createOption("Que la colonne est indexée automatiquement", false);
            q12.createOption("Que la colonne ne peut pas être modifiée", false);
            questionRepository.save(q12);

            Question q13 = new Question();
            q13.setTitre("JOIN SQL");
            q13.setContenu("Quel type de JOIN retourne toutes les lignes des deux tables, même sans correspondance ?");
            q13.setCategorie(Question.Categorie.SQL);
            q13.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q13.setPoints(10);
            q13.createOption("INNER JOIN", false);
            q13.createOption("LEFT JOIN", false);
            q13.createOption("FULL OUTER JOIN", true);
            q13.createOption("CROSS JOIN", false);
            questionRepository.save(q13);

            Question q14 = new Question();
            q14.setTitre("GROUP BY et HAVING");
            q14.setContenu("Quelle est la différence entre WHERE et HAVING en SQL ?");
            q14.setCategorie(Question.Categorie.SQL);
            q14.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q14.setPoints(10);
            q14.createOption("Il n'y a aucune différence", false);
            q14.createOption("WHERE filtre les lignes avant groupement, HAVING filtre après groupement", true);
            q14.createOption("HAVING s'utilise sans GROUP BY", false);
            q14.createOption("WHERE s'utilise uniquement avec des nombres", false);
            questionRepository.save(q14);

            Question q15 = new Question();
            q15.setTitre("Index SQL");
            q15.setContenu("Quel est l'impact principal d'un index sur une table SQL ?");
            q15.setCategorie(Question.Categorie.SQL);
            q15.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q15.setPoints(10);
            q15.createOption("Accélère les SELECT mais peut ralentir les INSERT/UPDATE", true);
            q15.createOption("Accélère toutes les opérations sans inconvénient", false);
            q15.createOption("Réduit la taille de la table", false);
            q15.createOption("Empêche les doublons", false);
            questionRepository.save(q15);

            Question q16 = new Question();
            q16.setTitre("Transactions SQL");
            q16.setContenu("Que garantit le principe ACID dans une transaction SQL ?");
            q16.setCategorie(Question.Categorie.SQL);
            q16.setNiveau(Question.Niveau.EXPERT);
            q16.setPoints(15);
            q16.createOption("Atomicité, Cohérence, Isolation, Durabilité", true);
            q16.createOption("Automatisation, Contrôle, Intégrité, Distribution", false);
            q16.createOption("Accès, Cohérence, Index, Données", false);
            q16.createOption("Atomicité, Compression, Isolation, Disponibilité", false);
            questionRepository.save(q16);

            Question q17 = new Question();
            q17.setTitre("Fenêtres SQL");
            q17.setContenu("Quelle fonction SQL permet de calculer un rang sans dupliquer les lignes comme GROUP BY ?");
            q17.setCategorie(Question.Categorie.SQL);
            q17.setNiveau(Question.Niveau.EXPERT);
            q17.setPoints(15);
            q17.createOption("COUNT()", false);
            q17.createOption("RANK() OVER (PARTITION BY ...)", true);
            q17.createOption("GROUP_CONCAT()", false);
            q17.createOption("DISTINCT", false);
            questionRepository.save(q17);

            // ===================== QUESTIONS JAVASCRIPT =====================

            Question q18 = new Question();
            q18.setTitre("Déclaration de variables");
            q18.setContenu("Quelle déclaration de variable a une portée de bloc en JavaScript ?");
            q18.setCategorie(Question.Categorie.JAVASCRIPT);
            q18.setNiveau(Question.Niveau.DÉBUTANT);
            q18.setPoints(5);
            q18.createOption("var", false);
            q18.createOption("let", true);
            q18.createOption("variable", false);
            q18.createOption("dim", false);
            questionRepository.save(q18);

            Question q19 = new Question();
            q19.setTitre("Type de données");
            q19.setContenu("Quel est le résultat de typeof null en JavaScript ?");
            q19.setCategorie(Question.Categorie.JAVASCRIPT);
            q19.setNiveau(Question.Niveau.DÉBUTANT);
            q19.setPoints(5);
            q19.createOption("'null'", false);
            q19.createOption("'undefined'", false);
            q19.createOption("'object'", true);
            q19.createOption("'boolean'", false);
            questionRepository.save(q19);

            Question q20 = new Question();
            q20.setTitre("Fonctions fléchées");
            q20.setContenu("Quelle est la syntaxe correcte d'une fonction fléchée qui retourne x * 2 ?");
            q20.setCategorie(Question.Categorie.JAVASCRIPT);
            q20.setNiveau(Question.Niveau.DÉBUTANT);
            q20.setPoints(5);
            q20.createOption("function(x) => x * 2", false);
            q20.createOption("x => x * 2", true);
            q20.createOption("(x) -> x * 2", false);
            q20.createOption("lambda x: x * 2", false);
            questionRepository.save(q20);

            Question q21 = new Question();
            q21.setTitre("Promesses JavaScript");
            q21.setContenu("Quelle méthode permet d'attendre plusieurs Promises en parallèle ?");
            q21.setCategorie(Question.Categorie.JAVASCRIPT);
            q21.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q21.setPoints(10);
            q21.createOption("Promise.wait()", false);
            q21.createOption("Promise.all()", true);
            q21.createOption("Promise.sync()", false);
            q21.createOption("await.all()", false);
            questionRepository.save(q21);

            Question q22 = new Question();
            q22.setTitre("Event Loop");
            q22.setContenu("Dans quel ordre sont exécutés les callbacks en JavaScript ?");
            q22.setCategorie(Question.Categorie.JAVASCRIPT);
            q22.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q22.setPoints(10);
            q22.createOption("Dans l'ordre d'arrivée sans priorité", false);
            q22.createOption("Les microtasks (Promises) avant les macrotasks (setTimeout)", true);
            q22.createOption("Les macrotasks avant les microtasks", false);
            q22.createOption("De façon aléatoire", false);
            questionRepository.save(q22);

            Question q23 = new Question();
            q23.setTitre("Closure JavaScript");
            q23.setContenu("Qu'est-ce qu'une closure en JavaScript ?");
            q23.setCategorie(Question.Categorie.JAVASCRIPT);
            q23.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q23.setPoints(10);
            q23.createOption("Une fonction qui ne retourne rien", false);
            q23.createOption("Une fonction qui se rappelle elle-même", false);
            q23.createOption("Une fonction qui a accès aux variables de sa portée parente même après son exécution", true);
            q23.createOption("Une fonction anonyme", false);
            questionRepository.save(q23);

            Question q24 = new Question();
            q24.setTitre("Prototype JavaScript");
            q24.setContenu("Comment fonctionne l'héritage en JavaScript ?");
            q24.setCategorie(Question.Categorie.JAVASCRIPT);
            q24.setNiveau(Question.Niveau.EXPERT);
            q24.setPoints(15);
            q24.createOption("Par héritage classique comme Java", false);
            q24.createOption("Par chaîne de prototypes (prototype chain)", true);
            q24.createOption("Par composition uniquement", false);
            q24.createOption("JavaScript ne supporte pas l'héritage", false);
            questionRepository.save(q24);

            Question q25 = new Question();
            q25.setTitre("WeakMap vs Map");
            q25.setContenu("Quelle est la principale différence entre Map et WeakMap en JavaScript ?");
            q25.setCategorie(Question.Categorie.JAVASCRIPT);
            q25.setNiveau(Question.Niveau.EXPERT);
            q25.setPoints(15);
            q25.createOption("WeakMap est plus rapide", false);
            q25.createOption("WeakMap ne supporte que les strings comme clés", false);
            q25.createOption("WeakMap garde des références faibles et permet le garbage collection des clés", true);
            q25.createOption("Il n'y a aucune différence", false);
            questionRepository.save(q25);

            // ===================== QUESTIONS SPRING =====================

            Question q26 = new Question();
            q26.setTitre("Injection de dépendances");
            q26.setContenu("Quelle annotation Spring indique qu'une classe est un composant géré par le conteneur IoC ?");
            q26.setCategorie(Question.Categorie.SPRING);
            q26.setNiveau(Question.Niveau.DÉBUTANT);
            q26.setPoints(5);
            q26.createOption("@Inject", false);
            q26.createOption("@Component", true);
            q26.createOption("@Bean", false);
            q26.createOption("@Dependency", false);
            questionRepository.save(q26);

            Question q27 = new Question();
            q27.setTitre("Spring Boot annotation principale");
            q27.setContenu("Quelle annotation est utilisée sur la classe principale d'une application Spring Boot ?");
            q27.setCategorie(Question.Categorie.SPRING);
            q27.setNiveau(Question.Niveau.DÉBUTANT);
            q27.setPoints(5);
            q27.createOption("@SpringApplication", false);
            q27.createOption("@EnableSpringBoot", false);
            q27.createOption("@SpringBootApplication", true);
            q27.createOption("@BootApplication", false);
            questionRepository.save(q27);

            Question q28 = new Question();
            q28.setTitre("REST Controller");
            q28.setContenu("Quelle annotation combine @Controller et @ResponseBody dans Spring ?");
            q28.setCategorie(Question.Categorie.SPRING);
            q28.setNiveau(Question.Niveau.DÉBUTANT);
            q28.setPoints(5);
            q28.createOption("@ApiController", false);
            q28.createOption("@RestController", true);
            q28.createOption("@WebController", false);
            q28.createOption("@JsonController", false);
            questionRepository.save(q28);

            Question q29 = new Question();
            q29.setTitre("Spring JPA Repository");
            q29.setContenu("Quelle interface Spring Data JPA fournit les opérations CRUD de base ?");
            q29.setCategorie(Question.Categorie.SPRING);
            q29.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q29.setPoints(10);
            q29.createOption("CrudRepository", false);
            q29.createOption("JpaRepository", true);
            q29.createOption("DataRepository", false);
            q29.createOption("EntityRepository", false);
            questionRepository.save(q29);

            Question q30 = new Question();
            q30.setTitre("Spring Security - JWT");
            q30.setContenu("Dans quelle couche doit-on placer le filtre JWT dans Spring Security ?");
            q30.setCategorie(Question.Categorie.SPRING);
            q30.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q30.setPoints(10);
            q30.createOption("Après DispatcherServlet", false);
            q30.createOption("Avant UsernamePasswordAuthenticationFilter", true);
            q30.createOption("Après AuthorizationFilter", false);
            q30.createOption("La position n'a pas d'importance", false);
            questionRepository.save(q30);

            Question q31 = new Question();
            q31.setTitre("Transactions Spring");
            q31.setContenu("Que se passe-t-il si une RuntimeException est levée dans une méthode @Transactional ?");
            q31.setCategorie(Question.Categorie.SPRING);
            q31.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q31.setPoints(10);
            q31.createOption("La transaction est committée", false);
            q31.createOption("La transaction est rollback automatiquement", true);
            q31.createOption("Spring ignore l'exception", false);
            q31.createOption("La méthode est réexécutée", false);
            questionRepository.save(q31);

            Question q32 = new Question();
            q32.setTitre("Spring AOP");
            q32.setContenu("Quel type d'advice AOP s'exécute autour d'une méthode et contrôle son exécution ?");
            q32.setCategorie(Question.Categorie.SPRING);
            q32.setNiveau(Question.Niveau.EXPERT);
            q32.setPoints(15);
            q32.createOption("@Before", false);
            q32.createOption("@After", false);
            q32.createOption("@Around", true);
            q32.createOption("@AfterReturning", false);
            questionRepository.save(q32);

            Question q33 = new Question();
            q33.setTitre("Spring Profiles");
            q33.setContenu("Comment activer un profil Spring Boot spécifique au démarrage en ligne de commande ?");
            q33.setCategorie(Question.Categorie.SPRING);
            q33.setNiveau(Question.Niveau.EXPERT);
            q33.setPoints(15);
            q33.createOption("--spring.profile=prod", false);
            q33.createOption("-Dspring.profiles.active=prod", true);
            q33.createOption("--active-profile=prod", false);
            q33.createOption("--profile prod", false);
            questionRepository.save(q33);

            // ===================== QUESTIONS DOCKER =====================

            Question q34 = new Question();
            q34.setTitre("Image vs Conteneur");
            q34.setContenu("Quelle est la différence entre une image Docker et un conteneur Docker ?");
            q34.setCategorie(Question.Categorie.DOCKER);
            q34.setNiveau(Question.Niveau.DÉBUTANT);
            q34.setPoints(5);
            q34.createOption("Ce sont la même chose", false);
            q34.createOption("Une image est un template immuable, un conteneur est une instance en cours d'exécution", true);
            q34.createOption("Un conteneur est plus grand qu'une image", false);
            q34.createOption("Une image s'exécute, un conteneur se stocke", false);
            questionRepository.save(q34);

            Question q35 = new Question();
            q35.setTitre("Commande docker run");
            q35.setContenu("Quelle option de 'docker run' permet de mapper le port 8080 de l'hôte vers le port 8080 du conteneur ?");
            q35.setCategorie(Question.Categorie.DOCKER);
            q35.setNiveau(Question.Niveau.DÉBUTANT);
            q35.setPoints(5);
            q35.createOption("-p 8080:8080", true);
            q35.createOption("--port 8080", false);
            q35.createOption("-map 8080:8080", false);
            q35.createOption("--expose 8080:8080", false);
            questionRepository.save(q35);

            Question q36 = new Question();
            q36.setTitre("Dockerfile FROM");
            q36.setContenu("Que signifie l'instruction FROM dans un Dockerfile ?");
            q36.setCategorie(Question.Categorie.DOCKER);
            q36.setNiveau(Question.Niveau.DÉBUTANT);
            q36.setPoints(5);
            q36.createOption("Définit le répertoire de travail", false);
            q36.createOption("Spécifie l'image de base à utiliser", true);
            q36.createOption("Copie des fichiers dans l'image", false);
            q36.createOption("Définit les variables d'environnement", false);
            questionRepository.save(q36);

            Question q37 = new Question();
            q37.setTitre("Docker Compose");
            q37.setContenu("Quelle commande Docker Compose démarre les services en arrière-plan ?");
            q37.setCategorie(Question.Categorie.DOCKER);
            q37.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q37.setPoints(10);
            q37.createOption("docker compose start", false);
            q37.createOption("docker compose up -d", true);
            q37.createOption("docker compose run --background", false);
            q37.createOption("docker compose launch", false);
            questionRepository.save(q37);

            Question q38 = new Question();
            q38.setTitre("Volumes Docker");
            q38.setContenu("Pourquoi utiliser un volume Docker pour une base de données ?");
            q38.setCategorie(Question.Categorie.DOCKER);
            q38.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q38.setPoints(10);
            q38.createOption("Pour accélérer les requêtes", false);
            q38.createOption("Pour persister les données au-delà du cycle de vie du conteneur", true);
            q38.createOption("Pour partager le réseau entre conteneurs", false);
            q38.createOption("Pour réduire la taille de l'image", false);
            questionRepository.save(q38);

            Question q39 = new Question();
            q39.setTitre("Multi-stage build");
            q39.setContenu("Quel est l'avantage principal d'un multi-stage build dans un Dockerfile ?");
            q39.setCategorie(Question.Categorie.DOCKER);
            q39.setNiveau(Question.Niveau.EXPERT);
            q39.setPoints(15);
            q39.createOption("Permet de builder plus rapidement", false);
            q39.createOption("Réduit la taille de l'image finale en séparant build et runtime", true);
            q39.createOption("Permet d'utiliser plusieurs images de base simultanément", false);
            q39.createOption("Améliore la sécurité du réseau", false);
            questionRepository.save(q39);

            Question q40 = new Question();
            q40.setTitre("Docker networking");
            q40.setContenu("Comment deux conteneurs dans le même réseau Docker peuvent-ils communiquer ?");
            q40.setCategorie(Question.Categorie.DOCKER);
            q40.setNiveau(Question.Niveau.EXPERT);
            q40.setPoints(15);
            q40.createOption("Uniquement via localhost", false);
            q40.createOption("Via le nom du service comme hostname", true);
            q40.createOption("Uniquement via l'adresse IP publique", false);
            q40.createOption("Les conteneurs ne peuvent pas communiquer directement", false);
            questionRepository.save(q40);

            // ===================== QUESTIONS GIT =====================

            Question q41 = new Question();
            q41.setTitre("Commande git init");
            q41.setContenu("Que fait la commande 'git init' ?");
            q41.setCategorie(Question.Categorie.GIT);
            q41.setNiveau(Question.Niveau.DÉBUTANT);
            q41.setPoints(5);
            q41.createOption("Clone un dépôt distant", false);
            q41.createOption("Initialise un nouveau dépôt Git local", true);
            q41.createOption("Crée une nouvelle branche", false);
            q41.createOption("Synchronise avec le serveur distant", false);
            questionRepository.save(q41);

            Question q42 = new Question();
            q42.setTitre("git add vs git commit");
            q42.setContenu("Quelle est la différence entre 'git add' et 'git commit' ?");
            q42.setCategorie(Question.Categorie.GIT);
            q42.setNiveau(Question.Niveau.DÉBUTANT);
            q42.setPoints(5);
            q42.createOption("Ce sont des synonymes", false);
            q42.createOption("git add ajoute au dépôt distant, git commit sauvegarde en local", false);
            q42.createOption("git add place les fichiers dans la zone de staging, git commit enregistre un snapshot", true);
            q42.createOption("git add crée une branche, git commit la fusionne", false);
            questionRepository.save(q42);

            Question q43 = new Question();
            q43.setTitre("git pull vs git fetch");
            q43.setContenu("Quelle est la différence entre 'git pull' et 'git fetch' ?");
            q43.setCategorie(Question.Categorie.GIT);
            q43.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q43.setPoints(10);
            q43.createOption("git fetch télécharge et fusionne, git pull télécharge seulement", false);
            q43.createOption("git pull télécharge et fusionne, git fetch télécharge sans fusionner", true);
            q43.createOption("Il n'y a aucune différence", false);
            q43.createOption("git pull s'utilise en local, git fetch avec le distant", false);
            questionRepository.save(q43);

            Question q44 = new Question();
            q44.setTitre("Branches Git");
            q44.setContenu("Quelle commande crée une nouvelle branche ET se positionne dessus ?");
            q44.setCategorie(Question.Categorie.GIT);
            q44.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q44.setPoints(10);
            q44.createOption("git branch ma-branche", false);
            q44.createOption("git new ma-branche", false);
            q44.createOption("git checkout -b ma-branche", true);
            q44.createOption("git switch --create ma-branche", false);
            questionRepository.save(q44);

            Question q45 = new Question();
            q45.setTitre("git merge vs git rebase");
            q45.setContenu("Quelle est la différence principale entre merge et rebase ?");
            q45.setCategorie(Question.Categorie.GIT);
            q45.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q45.setPoints(10);
            q45.createOption("merge réécrit l'historique, rebase le conserve", false);
            q45.createOption("rebase réécrit l'historique pour un historique linéaire, merge conserve le contexte de fusion", true);
            q45.createOption("Il n'y a aucune différence", false);
            q45.createOption("rebase est plus rapide que merge", false);
            questionRepository.save(q45);

            Question q46 = new Question();
            q46.setTitre("git stash");
            q46.setContenu("À quoi sert 'git stash' ?");
            q46.setCategorie(Question.Categorie.GIT);
            q46.setNiveau(Question.Niveau.INTERMÉDIAIRE);
            q46.setPoints(10);
            q46.createOption("Supprime les fichiers non trackés", false);
            q46.createOption("Sauvegarde temporairement les modifications non committées", true);
            q46.createOption("Archive l'historique des commits", false);
            q46.createOption("Crée une copie du dépôt", false);
            questionRepository.save(q46);

            Question q47 = new Question();
            q47.setTitre("git cherry-pick");
            q47.setContenu("Que fait la commande 'git cherry-pick <hash>' ?");
            q47.setCategorie(Question.Categorie.GIT);
            q47.setNiveau(Question.Niveau.EXPERT);
            q47.setPoints(15);
            q47.createOption("Supprime un commit spécifique", false);
            q47.createOption("Applique les changements d'un commit spécifique sur la branche courante", true);
            q47.createOption("Fusionne deux branches", false);
            q47.createOption("Réinitialise la branche à un commit précédent", false);
            questionRepository.save(q47);

            Question q48 = new Question();
            q48.setTitre("git reset vs git revert");
            q48.setContenu("Quelle commande est préférable pour annuler un commit déjà pushé sur un dépôt partagé ?");
            q48.setCategorie(Question.Categorie.GIT);
            q48.setNiveau(Question.Niveau.EXPERT);
            q48.setPoints(15);
            q48.createOption("git reset --hard", false);
            q48.createOption("git revert", true);
            q48.createOption("git checkout", false);
            q48.createOption("git clean", false);
            questionRepository.save(q48);

            // ===================== TESTS =====================

            // Test Java Débutant
            Test testJavaDebutant = new Test();
            testJavaDebutant.setTitre("Java - Les Fondamentaux");
            testJavaDebutant.setDescription("Testez vos connaissances de base en Java : JVM, types, modificateurs d'accès et boucles.");
            testJavaDebutant.setDureeMinutes(15);
            testJavaDebutant.addQuestion(q1);
            testJavaDebutant.addQuestion(q2);
            testJavaDebutant.addQuestion(q3);
            testJavaDebutant.addQuestion(q4);
            testRepository.save(testJavaDebutant);

            // Test Java Avancé
            Test testJavaAvance = new Test();
            testJavaAvance.setTitre("Java - Niveau Avancé");
            testJavaAvance.setDescription("Interfaces, collections, exceptions, Streams et Garbage Collector.");
            testJavaAvance.setDureeMinutes(25);
            testJavaAvance.addQuestion(q5);
            testJavaAvance.addQuestion(q6);
            testJavaAvance.addQuestion(q7);
            testJavaAvance.addQuestion(q8);
            testJavaAvance.addQuestion(q9);
            testRepository.save(testJavaAvance);

            // Test SQL Complet
            Test testSQL = new Test();
            testSQL.setTitre("SQL - De Débutant à Expert");
            testSQL.setDescription("Maîtrisez SQL : SELECT, JOIN, GROUP BY, index, transactions et fenêtres.");
            testSQL.setDureeMinutes(30);
            testSQL.addQuestion(q10);
            testSQL.addQuestion(q11);
            testSQL.addQuestion(q12);
            testSQL.addQuestion(q13);
            testSQL.addQuestion(q14);
            testSQL.addQuestion(q15);
            testSQL.addQuestion(q16);
            testSQL.addQuestion(q17);
            testRepository.save(testSQL);

            // Test JavaScript
            Test testJS = new Test();
            testJS.setTitre("JavaScript - Fondamentaux et Avancé");
            testJS.setDescription("Variables, types, fonctions fléchées, Promises, Event Loop, closures et prototypes.");
            testJS.setDureeMinutes(25);
            testJS.addQuestion(q18);
            testJS.addQuestion(q19);
            testJS.addQuestion(q20);
            testJS.addQuestion(q21);
            testJS.addQuestion(q22);
            testJS.addQuestion(q23);
            testJS.addQuestion(q24);
            testJS.addQuestion(q25);
            testRepository.save(testJS);

            // Test Spring Boot
            Test testSpring = new Test();
            testSpring.setTitre("Spring Boot - Complet");
            testSpring.setDescription("IoC, annotations, REST, JPA, Security, transactions, AOP et profiles.");
            testSpring.setDureeMinutes(30);
            testSpring.addQuestion(q26);
            testSpring.addQuestion(q27);
            testSpring.addQuestion(q28);
            testSpring.addQuestion(q29);
            testSpring.addQuestion(q30);
            testSpring.addQuestion(q31);
            testSpring.addQuestion(q32);
            testSpring.addQuestion(q33);
            testRepository.save(testSpring);

            // Test Docker
            Test testDocker = new Test();
            testDocker.setTitre("Docker - De Zéro à Expert");
            testDocker.setDescription("Images, conteneurs, Dockerfile, Docker Compose, volumes, networking et multi-stage build.");
            testDocker.setDureeMinutes(25);
            testDocker.addQuestion(q34);
            testDocker.addQuestion(q35);
            testDocker.addQuestion(q36);
            testDocker.addQuestion(q37);
            testDocker.addQuestion(q38);
            testDocker.addQuestion(q39);
            testDocker.addQuestion(q40);
            testRepository.save(testDocker);

            // Test Git
            Test testGit = new Test();
            testGit.setTitre("Git - Maîtrise du Versioning");
            testGit.setDescription("init, add, commit, branches, merge, rebase, stash, cherry-pick et revert.");
            testGit.setDureeMinutes(25);
            testGit.addQuestion(q41);
            testGit.addQuestion(q42);
            testGit.addQuestion(q43);
            testGit.addQuestion(q44);
            testGit.addQuestion(q45);
            testGit.addQuestion(q46);
            testGit.addQuestion(q47);
            testGit.addQuestion(q48);
            testRepository.save(testGit);

            // Test Full Stack
            Test testFullStack = new Test();
            testFullStack.setTitre("Full Stack - Le Grand Test");
            testFullStack.setDescription("Un test complet couvrant Java, SQL, JavaScript, Spring, Docker et Git.");
            testFullStack.setDureeMinutes(45);
            testFullStack.addQuestion(q2);  // Java
            testFullStack.addQuestion(q5);  // Java avancé
            testFullStack.addQuestion(q13); // SQL JOIN
            testFullStack.addQuestion(q16); // SQL ACID
            testFullStack.addQuestion(q21); // JS Promises
            testFullStack.addQuestion(q23); // JS Closure
            testFullStack.addQuestion(q29); // Spring JPA
            testFullStack.addQuestion(q30); // Spring Security
            testFullStack.addQuestion(q37); // Docker Compose
            testFullStack.addQuestion(q39); // Docker multi-stage
            testFullStack.addQuestion(q45); // Git rebase
            testFullStack.addQuestion(q48); // Git revert
            testRepository.save(testFullStack);

            System.out.println("✅ Données initialisées : 48 questions, 8 tests, 2 utilisateurs");
        };
    }
}