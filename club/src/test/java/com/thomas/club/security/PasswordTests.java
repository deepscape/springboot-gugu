package com.thomas.club.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncoder() {
        String password = "1111";
        String enPw = passwordEncoder.encode(password);
        System.out.println("enPw: " + enPw);

        boolean matchResult = passwordEncoder.matches(password, enPw);
        System.out.println("matchResult: " + matchResult);
    }

    @Test
    public void testEncoder2() {
        String password = "1111";
        String enPw = "$2a$10$x1XgqJHtADRAVaPMMvLwQu.uk/iT/4.1kkMsqufMhUZHXbJ.wmOZi";
        System.out.println("enPw: " + enPw);

        boolean matchResult = passwordEncoder.matches(password, enPw);
        System.out.println("matchResult: " + matchResult);
    }
}