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

    @Bean
    CommandLineRunner test(MemberRepo repo) {
        return args -> {
            if (repo.count() == 0) {
                Member m = Member.builder()
                        .username("Test User")
                        .passwordHash("1234")
                        .role("Member")
                        .build();
                repo.save(m);
                System.out.println("✅ Member saved to DB!");
            } else {
                System.out.println("Members in DB: " + repo.count());
            }
        };
    }
}
