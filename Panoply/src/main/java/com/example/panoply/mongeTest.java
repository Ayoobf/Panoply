package com.example.panoply;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class mongeTest {
    public static void main(String[] args) {
        String uri = "mongodb+srv://Ayoobf:ptU7wwXkC8EP4VZl@documentmanagercluster.ewmjoau.mongodb.net/?retryWrites=true&w=majority&appName=AtlasApp";

        String databaseName = "dms_collections";
        String databaseUsers = "users";
        String databaseTeams = "teams";
        String databaseDocuments = "documents";

        try (MongoClient mongoClient = MongoClients.create(uri)){
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(databaseUsers);

            Document doc = collection.find(eq("username", "user123")).first();
            if (doc != null ){
                System.out.println(doc.toJson());
            }else {
                System.out.println("no matching Documents found");
            }
        }


    }
}
