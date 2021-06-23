package com.thomas.club.security;

import com.thomas.club.security.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JWTTests {

    private JWTUtil jwtUtil;

    // Spring 이 아니므로, 객체 직접 생성
    @BeforeEach
    public void testBefore() {
        System.out.println("testBefore........................");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() throws Exception {
        String email = "user95@thomas.com";
        String str = jwtUtil.generateToken(email);
        System.out.println(str);
    }

    @Test
    public void testValidate() throws Exception {
        String email = "user95@thomas.com";
        String str = jwtUtil.generateToken(email);

        // 만료 기간을 1초로 지정하고, 만료 후에 Exception 발생 여부를 테스트
        Thread.sleep(5000);

        String resultEmail = jwtUtil.validateAndExtract(str);
        System.out.println(resultEmail);
    }

}