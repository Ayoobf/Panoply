package com.example.panoply.exampleCode;

import com.example.panoply.mongoDB.MongoDBHandlerExtra;

public class testlogin2 {


    public static void main(String[] args) {
        MongoDBHandlerExtra md = new MongoDBHandlerExtra();
        System.out.println(
                md.authenticateUser("rflooivl@gmail.com", "12345Test"));
    }
}