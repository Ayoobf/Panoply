package com.example.panoply.exampleCode;


import com.example.panoply.mongoDB.MongoDBHandler;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class mongeTest {
    public static void main(String[] args) {
        String uri = "";

        String databaseName = "dms_collections";

        MongoDBHandler handler = new MongoDBHandler(uri, databaseName);
        handler.insertUser("rflooivl@gmail.com",
                "12345Test",
                new ObjectId("651aff049f8c00e5a601cd33"),
                true,
                "Ayoob",
                "Florival",
                "1234567890");

    }
}
