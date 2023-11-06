package com.example.panoply.exampleCode;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

// Used for
public class encryptionExample {

    public static void main(String[] args) {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        String hashedPassword = bc.encode("password");
        boolean tets = bc.matches("password", hashedPassword);
        System.out.println(tets);

    }

}
