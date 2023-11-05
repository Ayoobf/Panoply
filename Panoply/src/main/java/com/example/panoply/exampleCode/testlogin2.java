package com.example.panoply.exampleCode;

import com.example.panoply.mongoDB.MongoDBHandlerExtra;

import org.bson.types.ObjectId;

public class testlogin2 {


    public static void main(String[] args) {
        MongoDBHandlerExtra md = new MongoDBHandlerExtra();

//        md.signUpUser("ayoob", "florival", "rflooivl@gmail.com", "123123", false, teamId,"8484599622");
        md.updateUser("65472cd07b94685fcf2efcfa", "65472cd07b94685fcf2efcfb");
    }
}
