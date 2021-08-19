// DBRead.java
// singleton class to handle MongoDB operations

package edu.ucr.cs.bdlab.raptor;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

class DBRead {

    // single instance
    private static DBRead singleInstance = null;

    // MongoDB client
    //MongoClient mongoClient;

    // private constructor
    private DBRead() {

        // try connecting to the MongoDB cluster
        // mongodb.uri is a system variable that needs to be set when you run mvn compile exec:java
        // something like this:
        // mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Read" -Dmongodb.uri="<my secret uri>"
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            System.out.println("----connected to MongoDB cluster successfully");
        }
    }

    public static void read() {

        /*MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
        MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");

        // find one document
        Document student1 = gradesCollection.find(new Document("student_id", 9000)).first();
        System.out.println("Student 1: " + student1.toJson());*/
        System.out.println("----reading");
    }

    // get or create instance of DBRead class
    public static DBRead getInstance() {
        if (singleInstance == null) {
            singleInstance = new DBRead();
        }
        return singleInstance;
    }
}