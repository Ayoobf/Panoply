package com.example.panoply.exampleCode;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Used for
public class encryptionExample {

    public static void main(String[] args) {
        String rawPassword = "123";
        String dbPass;

        BCryptPasswordEncoder pa = new BCryptPasswordEncoder();
        dbPass = pa.encode("123");
        System.out.println(pa.matches(rawPassword, dbPass));

    }

}
