package com.test.passwords;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PwdGenTest {
    @Test
    public void generatePwd() {
        var enc = new BCryptPasswordEncoder();
//        System.out.println(enc.encode("blank"));
        System.out.println(enc.encode("pwd"));

    }
}
