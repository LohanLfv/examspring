package com.lohan.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

    /**
     * Point de terminaison web qui retourne un message d'accueil.
     * Accessible à la racine de l'URL du serveur (ex: http://localhost:8080/).
     * @return Une chaîne de caractères de salutation.
     */
    @GetMapping("/")
    public String hello() {
        return "Hello World, Lohan Lefevre test pipeline";
    }

    /**
     * Méthode principale qui démarre l'application Spring Boot.
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
