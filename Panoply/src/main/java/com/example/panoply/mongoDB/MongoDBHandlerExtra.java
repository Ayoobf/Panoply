package com.example.panoply.mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class MongoDBHandlerExtra {
    final static String PASSWORD = System.getenv("PASSWORD");
    static String databaseName = "dms_collections";
    static String uri = "mongodb+srv://ayoobf:" + PASSWORD + "@documentmanagercluster.ewmjoau.mongodb.net/?retryWrites=true&w=majority&appName=AtlasApp";
    static MongoClient client = MongoClients.create(uri);
    static MongoDatabase database = client.getDatabase(databaseName);
    static MongoCollection<Document> collection = database.getCollection("users");

    public MongoDBHandlerExtra() {

    }

    public int authenticateUser(String username, String password) {

        Document query = new Document("username", username);
        Document retrievedDoc = collection.find(query).first();

        if (retrievedDoc != null) {

            String storedPassword = retrievedDoc.getString("password");

            if (password.equals(storedPassword)) {
                return 1;
            }
        }
        return 0;
    }
}
