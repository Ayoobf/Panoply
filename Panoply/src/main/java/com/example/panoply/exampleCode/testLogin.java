package com.example.panoply.exampleCode;

import com.example.panoply.mongoDB.MongoDBHandler;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Scanner;

public class testLogin {

    final static String PASSWORD = System.getenv("PASSWORD");
    static String databaseName = "dms_collections";
    static String uri = "mongodb+srv://ayoobf:" + PASSWORD + "@documentmanagercluster.ewmjoau.mongodb.net/?retryWrites=true&w=majority&appName=AtlasApp";
    static MongoClient client = MongoClients.create(uri);
    static MongoDatabase database = client.getDatabase(databaseName);
    static MongoCollection<Document> collection = database.getCollection("users");


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Login (L) or Sign Up (S):");
        String decision = scanner.nextLine();

        boolean goodInput = false;
        boolean login = false;
        boolean signUp = false;


        while (!goodInput) {
            if (decision.equals("L") || decision.equals("l")) {
                goodInput = true;
                login = true;

            } else if (decision.equals("S") | decision.equals("s")) {
                goodInput = true;
                signUp = true;

            } else {
                System.out.println("Irregular Input");
                System.out.println("Login (L) or Sign Up (S):");
                decision = scanner.nextLine();
            }
        }

        if (login) {
            login();
        }
        if (signUp) {
            signUp();
            System.out.println("now that you created your account, log in!");
            login();
        }
        scanner.close();
    }


    // method for signing up new people
    private static void signUp() {

        // get user input
        Scanner scanner = new Scanner(System.in);

        System.out.print("username: ");
        String username = scanner.nextLine();
        String password = null;
        String confirmPassword = null;

        // confirm password
        while (true) {
            System.out.print("password: ");
             password = scanner.nextLine();

            System.out.print("Confirm Password: ");
             confirmPassword = scanner.nextLine();

            if (password.equals(confirmPassword)) {
                break;

            } else {
                System.out.println("Passwords dont match");
            }
        }

        // Check for if Admin
        System.out.print("Are yo an admin (Y/N): ");
        char isAdminC = scanner.nextLine().toUpperCase().charAt(0);
        boolean isAdmin = false;
        while (true){
            if (isAdminC == 'Y' ) {
                isAdmin = true;
                break;
            } else if ( isAdminC == 'N'){
                break;
            }else {
                System.out.println("incorrect response. Try again");
                System.out.print("Are yo an admin (Y/N): ");
                isAdminC = scanner.nextLine().toUpperCase().charAt(0);
            }
        }

        // send Data
        MongoDBHandler newUser = new MongoDBHandler(uri, databaseName);
        newUser.insertUser(username,
                password,
                new ObjectId("651aff049f8c00e5a601cd33"),
                isAdmin
                );

    }

    // Method to login
    private static void login() {
        Scanner scanner = new Scanner(System.in);

        // get user input
        System.out.print("username: ");
        String username = scanner.nextLine();
        System.out.print("password: ");
        String password = scanner.nextLine();

        // document containing username $username
        Document query = new Document("username", username);

        // document containing result of query
        Document retrievedDoc = collection.find(query).first();

        // if not empty
        if (retrievedDoc != null) {
            // Password from the database
            String storedPass = retrievedDoc.getString("password");

            if (password.equals(storedPass)) {
                System.out.println("Authentication Good");
            } else {
                System.out.println("bad Auth");
            }

        } else {
            System.out.println("No user found");
        }
        scanner.close();
    }
}
