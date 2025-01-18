package com.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Date;

public class JwtLibTest {
    @Test
    public void testJWT() {
        String jwtSecretKey = "meow_meaaaaaaaaaaaaaaaaaaaaaowzz123";

        String token = Jwts.builder()
                .id("uuuuuiiiiddd")
                .subject("max")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .claim("color", "yellow")
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes()))
                .compact();

        JwtParser parser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes())).build();
        Claims claims = parser.parseSignedClaims(token).getPayload();


        Date d1 = new Date(claims.getIssuedAt().getTime());
        Date d2 = new Date(claims.getExpiration().getTime());
        claims.get("color");
    }
    @Test
    public void genPwd() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode("admin_pwd");
        System.out.println(hashed);
    }
}
