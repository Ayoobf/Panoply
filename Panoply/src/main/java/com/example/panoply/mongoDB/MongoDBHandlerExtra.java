package com.example.panoply.mongoDB;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MongoDBHandlerExtra {
    final static String PASSWORD = System.getenv("PASSWORD");
    private static String databaseName = "dms_collections";
    private static String uri = "mongodb+srv://ayoobf:" + PASSWORD + "@documentmanagercluster.ewmjoau.mongodb.net/?retryWrites=true&w=majority&appName=AtlasApp";
    private static MongoClient client = MongoClients.create(uri);
    private static MongoDatabase database = client.getDatabase(databaseName);
    private static MongoCollection<Document> userCollection = database.getCollection("users");
    private static MongoCollection<Document> teamsCollection = database.getCollection("teams");
    private static BCryptPasswordEncoder authenticator = new BCryptPasswordEncoder();
    String databaseUsers = "users";
    String databaseTeams = "teams";

    public MongoDBHandlerExtra() {

    }

    public int authenticateUser(String username, String password) {

        Document query = new Document("username", username);
        Document retrievedDoc = userCollection.find(query).first();

        if (retrievedDoc != null) {

            String storedPassword = retrievedDoc.getString("password");

            if (authenticator.matches(password, storedPassword)) {
                return 1;
            }
        }
        return 0;
    }

    public void signUpUser(String firstName, String lastName, String email, String password, boolean isAdmin, String teamId, String phoneNumber) {

        MongoCollection<Document> collection = database.getCollection(databaseUsers);
        String securePassword = authenticator.encode(password);

        collection.insertOne(new Document()
                .append("_id", new ObjectId())
                .append("username", email)
                .append("password", securePassword)
                .append("team_id", new ObjectId(teamId))
                .append("is_admin", isAdmin)
                .append("first_name", firstName)
                .append("last_name", lastName)
                .append("phone_number", phoneNumber)
        );


    }

    public void signUpUser(String firstName, String lastName, String email, String password, boolean isAdmin, String phoneNumber) {

        MongoCollection<Document> collection = database.getCollection(databaseUsers);

        String securePassword = authenticator.encode(password);

        collection.insertOne(new Document()
                .append("_id", new ObjectId())
                .append("username", email)
                .append("password", securePassword)
                .append("team_id", null)
                .append("is_admin", isAdmin)
                .append("first_name", firstName)
                .append("last_name", lastName)
                .append("phone_number", phoneNumber)
        );


    }

    public void updateUser(String userObjectId, String teamObjectId) {

        userCollection.updateOne(
                Filters.eq("_id", new ObjectId(userObjectId)),
                Updates.set("team_id", new ObjectId(teamObjectId)
                ));
    }

    public void makeTeam(String teamName, String adminName) {

        teamsCollection.insertOne(new Document()
                .append("_id", new ObjectId())
                .append("team_name", teamName)
                .append("team_size", 1)
                .append("admin", new ObjectId(adminName))
        );


    }

    public String findUser(String email) {
        Document doc = userCollection.find(Filters.eq("username", email)).first();

        if (doc != null) {
            return doc.getObjectId("_id").toString();

        }
        return null;
    }

    public String findTeam(String teamName) {
        Document doc = teamsCollection.find(Filters.eq("team_name", teamName)).first();

        if (doc != null) {
            return doc.getObjectId("_id").toString();

        }
        return null;


    }


}
