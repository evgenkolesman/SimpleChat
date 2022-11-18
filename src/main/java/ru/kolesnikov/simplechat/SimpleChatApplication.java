package ru.kolesnikov.simplechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kolesnikov.simplechat.repository.AuthRepository;
import ru.kolesnikov.simplechat.repository.MessageRepository;
import ru.kolesnikov.simplechat.repository.UserRepository;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableMongoRepositories(basePackageClasses = AuthRepository.class)
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, MessageRepository.class})
public class SimpleChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleChatApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
