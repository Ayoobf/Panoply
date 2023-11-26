package com.example.panoply.mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MongoDBHandlerExtra {
    final static String PASSWORD = System.getenv("PASSWORD");
    private static final String uri = "mongodb+srv://ayoobf:" + PASSWORD + "@documentmanagercluster.ewmjoau.mongodb.net/?retryWrites=true&w=majority&appName=AtlasApp";
    private static final MongoClient client = MongoClients.create(uri);
    private static final MongoDatabase database = client.getDatabase("dms_collections");
    private static final MongoCollection<Document> userCollection = database.getCollection("users");
    private static final MongoCollection<Document> teamsCollection = database.getCollection("teams");
    private static final BCryptPasswordEncoder authenticator = new BCryptPasswordEncoder();

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

        String securePassword = authenticator.encode(password);

        userCollection.insertOne(new Document()
                .append("_id", new ObjectId())
                .append("username", email)
                .append("password", securePassword)
                .append("team_id", new ObjectId(teamId))
                .append("is_admin", isAdmin)
                .append("first_name", firstName)
                .append("last_name", lastName)
                .append("phone_number", phoneNumber)
        );

        incrementTeamSize(teamId, findTeamSize(teamId));


    }

    public void signUpUser(String firstName, String lastName, String email, String password, boolean isAdmin, String phoneNumber) {


        String securePassword = authenticator.encode(password);

        userCollection.insertOne(new Document()
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

    // Finds User ObjectID
    public String findUser(String email) {
        Document doc = userCollection.find(Filters.eq("username", email)).first();

        if (doc != null) {
            return doc.getObjectId("_id").toString();

        }
        return null;
    }

    // Finds User FirstName (String)
    public String findUserFirstName(String userName) {
        return userCollection.distinct("first_name", Filters.eq("username", userName), String.class).first();

    }

    // Finds User LastName (String)
    public String findUserLastName(String userName) {
        return userCollection.distinct("last_name", Filters.eq("username", userName), String.class).first();

    }

    // Finds User PhoneNumber (String)
    public String findUserPhoneNumber(String userName) {
        return userCollection.distinct("phone_number", Filters.eq("username", userName), String.class).first();

    }

    // Finds User adminStatus (bool)
    public boolean findUserAdminStatus(String userName) {
        return Boolean.TRUE.equals(userCollection.distinct("is_admin", Filters.eq("username", userName), Boolean.class).first());

    }

    // Finds User TeamID via Username
    public String findUserTeamId(String userName) {
        return Objects.requireNonNull(userCollection.distinct("team_id", Filters.eq("username", userName), ObjectId.class).first()).toString();

    }

    // Returns Team Object
    public String findTeam(String teamName) {
        Document doc = teamsCollection.find(Filters.eq("team_name", teamName)).first();

        if (doc != null) {
            return doc.getObjectId("_id").toString();

        }
        return null;
    }

    // finds the size of a team with teamId
    public int findTeamSize(String teamIdStr) {
        ObjectId teamId = new ObjectId(teamIdStr);
        Integer result = teamsCollection.distinct("team_size", Filters.eq("_id", teamId), Integer.class).first();
        if (result != null) {
            return result;
        }
        return 0;

    }

    public void incrementTeamSize(String teamObjectId, int currentTeamSize) {
        teamsCollection.updateOne(
                Filters.eq("_id", new ObjectId(teamObjectId)),
                Updates.set("team_size", currentTeamSize + 1
                ));
    }

    public ArrayList<String> listUsers(String teamId) {
        List<Document> resultList = new ArrayList<>();
        userCollection.find(Filters.eq("team_id", new ObjectId(teamId))).into(resultList);

        ArrayList<String> users = new ArrayList<>();
        for (Document result : resultList) {
            users.add((String) result.get("username"));
        }

        return users;
    }

    // returns a list of user Objects
    public List<Document> listTeamMembers(String teamId) {
        List<Document> resultList = new ArrayList<>();
        return userCollection.find(Filters.eq("team_id", new ObjectId(teamId))).into(resultList);
    }


}
