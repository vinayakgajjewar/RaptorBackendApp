package edu.ucr.cs.bdlab.raptor;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class DBRead {

    public static void read() {

        // try connecting to the MongoDB cluster
        // mongodb.uri is a system variable that needs to be set when you run mvn compile exec:java
        // something like this:
        // mvn compile exec:java -Dexec.mainClass="com.mongodb.quickstart.Read" -Dmongodb.uri="<my secret uri>"
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");

            // find one document
            Document student1 = gradesCollection.find(new Document("student_id", 9000)).first();
            System.out.println("Student 1: " + student1.toJson());
        }
    }
}