package com.thomas.club.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private String secretKey = "thomas1234";

    // expire duration : 1 month
    private long expire = 60 * 24 * 30;

    // JWT Token 생성
    public String generateToken(String content) throws Exception {

        return Jwts.builder().setIssuedAt(new Date())
                             .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))     // 만료 기간 설정
                             .claim("sub", content)
                             .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))    // 키를 이용해 Signature 생성
                             .compact();
    }

    // 인코딩된 문자열에서 원하는 값을 추출하는 용도
    // JWT 문자열 검증
    public String validateAndExtract(String tokenStr) throws Exception {
        String contentValue = null;

        try {
            DefaultJws defaultJws = (DefaultJws) Jwts.parser()
                                                     .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                                                     .parseClaimsJws(tokenStr);

            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();
            log.info("-------------------------");

            contentValue = claims.getSubject();

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());

            contentValue = null;
        }

        return contentValue;
    }


}
