package com.ceofacebook.facebookv2;

import com.ceofacebook.facebookv2.entities.User;
import com.ceofacebook.facebookv2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class FacebookV2Application implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(FacebookV2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count()==0){
            User user = new User("LoganZ","loganz@hcmute.edu.vn","123456",
                    Arrays.asList("ROLE_ADMIN"));
            userRepository.save(user);
        }
    }

}
