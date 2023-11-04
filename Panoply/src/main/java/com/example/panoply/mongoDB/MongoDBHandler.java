package com.example.panoply.mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoDBHandler {

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoDBHandler(String connectionString, String databaseName) {

        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(databaseName);

    }

    public void insertUser(String userName, String password, ObjectId team_id, boolean isAdmin) {
        String databaseUsers = "users";

        MongoCollection<Document> collection = database.getCollection(databaseUsers);

        collection.insertOne(new Document()
                .append("_id", new ObjectId())
                .append("username", userName)
                .append("password", password)
                .append("team_id", team_id)
                .append("is_admin", isAdmin)
                .append("first_name", null)
                .append("last_name", null)
                .append("phone_number", null)
        );
    }

    public void insertUser(String userName, String password, ObjectId team_id, boolean isAdmin, String firstName, String lastName, String phone) {
        String databaseUsers = "users";

        MongoCollection<Document> collection = database.getCollection(databaseUsers);

        collection.insertOne(new Document()
                .append("_id", new ObjectId())
                .append("username", userName)
                .append("password", password)
                .append("team_id", team_id)
                .append("is_admin", isAdmin)
                .append("first_name", firstName)
                .append("last_name", lastName)
                .append("phone_number", phone)
        );
    }

    //TODO
    public void insertTeams() {
        String databaseTeams = "teams";

        MongoCollection<Document> collection = database.getCollection(databaseTeams);
        collection.insertOne(new Document()
                .append("", "")


        );


    }

    //TODO
    public void insertDocument() {
        String databaseDocuments = "documents";

        MongoCollection<Document> collection = database.getCollection(databaseDocuments);

        collection.insertOne(new Document()
                .append("", "")
        );
    }
}
