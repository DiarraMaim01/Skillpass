package com.skillpass.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        // 1. Nom du schéma de sécurité
        final String securitySchemeName = "bearerAuth";

        // 2. Création du schéma de sécurité JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("""
                    Entrez votre token JWT.
                    
                    Format attendu: Bearer [votre-token]
                    
                    Exemple: Bearer eyJhbGciOiJIUzI1NiJ9...
                    
                    Pour obtenir un token:
                    1. POST /api/auth/register (créer un compte)
                    2. POST /api/auth/login (se connecter)
                    """);

        // 3. Retourne la configuration OpenAPI
        return new OpenAPI()
                .info(new Info()
                        .title("API Skillpass")
                        .description("""
                            ## Plateforme d'évaluation technique
                            
                            ### 🔐 Authentification
                            Cette API utilise JWT pour sécuriser les endpoints.
                            
                            ### 👥 Rôles
                            - **USER** : peut consulter les questions et passer des tests
                            - **ADMIN** : peut créer/modifier/supprimer du contenu
                            
                            ### 📋 Comment utiliser Swagger avec JWT
                            1. Créez un compte via `/api/auth/register`
                            2. Connectez-vous via `/api/auth/login`
                            3. Copiez le token reçu
                            4. Cliquez sur le bouton **Authorize** (🔓) en bas
                            5. Entrez votre token au format: `Bearer votre-token`
                            6. Testez les endpoints protégés !
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Skillpass Team")
                                .email("maimouna_diarra@reseau.eseo.fr")
                                .url("https://github.com/DiarraMaim01"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName, securityScheme));
    }
}