package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class PasswordEncoderTest {

    static final String PASSWORD = "password";

    @Test
    public void testBCrypt(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        System.out.println(passwordEncoder.encode(PASSWORD));
        System.out.println(passwordEncoder.encode(PASSWORD));
    }

    @Test
    public void testSha256(){
        PasswordEncoder passwordEncoder = new StandardPasswordEncoder();

        System.out.println(passwordEncoder.encode(PASSWORD));
    }
}
