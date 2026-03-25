package com.flora.student.config;

import com.flora.student.model.Users;
import com.flora.student.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitailizer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitailizer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(!userRepository.existsByUsername("Admin")){
            Users user = new Users();
            user.setUsername("Admin");
            user.setPassword(passwordEncoder.encode("admin@123"));
            user.setActive(true);
            userRepository.save(user);
        }
    }
}
