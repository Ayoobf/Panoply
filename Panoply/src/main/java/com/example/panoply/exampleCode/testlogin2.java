package com.example.panoply.exampleCode;

import com.example.panoply.handlers.MongoDBHandler;

public class testlogin2 {


    public static void main(String[] args) {
        MongoDBHandler md = new MongoDBHandler();
        System.out.println(md.findTeamName("656a7e5642f83e29c95b7b3b"));

    }
}
