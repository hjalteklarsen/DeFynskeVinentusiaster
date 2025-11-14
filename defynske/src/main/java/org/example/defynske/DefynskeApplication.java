package org.example.defynske;

import org.example.defynske.model.Member;
import org.example.defynske.repo.MemberRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DefynskeApplication {
    public static void main(String[] args) {
        SpringApplication.run(DefynskeApplication.class, args);
    }
}
