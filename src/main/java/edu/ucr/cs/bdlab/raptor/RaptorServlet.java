//package org.bdlabucr;
package edu.ucr.cs.bdlab.raptor;

import java.io.IOException;

import java.util.*; // maps

// for some reason, including this import causes the servlet to break :(
//import java.nio.file.Paths; // paths

// jackson library to read/write json files
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ucr.cs.bdlab.beast.io.GeoJSONFeatureWriter;
import edu.ucr.cs.bdlab.beast.io.SpatialReader;
import edu.ucr.cs.bdlab.beast.JavaSpatialSparkContext;
import edu.ucr.cs.bdlab.beast.common.BeastOptions;
import edu.ucr.cs.bdlab.beast.geolite.IFeature;

import org.apache.spark.api.java.JavaRDD;
import org.apache.hadoop.conf.Configuration;
import org.apache.spark.api.java.JavaSparkContext;
// https://spark.apache.org/docs/latest/rdd-programming-guide.html
import org.apache.spark.SparkContext;
import org.apache.spark.SparkConf;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RaptorServlet extends HttpServlet {

    //protected Configuration conf;

    //protected BeastOptions opts;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //response.setContentType("application/json");
        response.setContentType("application/geo+json");
        response.setStatus(HttpServletResponse.SC_OK);

        // should this be a JavaSparkContext or a JavaSpatialSparkContext?
        // either way, it throws the same error

        SparkConf sparkconf = new SparkConf().setAppName("appName").setMaster("local[*]");

        //SparkContext sparkcontext = new SparkContext(sparkconf);

        //JavaSparkContext sc = new JavaSparkContext(sparkcontext);
        JavaSparkContext sc = new JavaSparkContext(sparkconf);
        JavaRDD<IFeature> records = SpatialReader.readInput(sc, new BeastOptions(), "exampleinput.geojson", "geojson");


        //JavaSpatialSparkContext sc = new JavaSpatialSparkContext();
        //JavaRDD<IFeature> records = sc.geojsonFile("exampleinput.geojson");

        try (GeoJSONFeatureWriter writer = new GeoJSONFeatureWriter()) {
            writer.initialize(response.getOutputStream(), new Configuration());
                writer.write(records.first());
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        //response.getWriter().println("{}");

        /*
        try (JsonGenerator jsonGenerator = new JsonFactory().createGenerator(response.getOutputStream())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("field1");
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("num1", 3);
            jsonGenerator.writeEndObject();
            jsonGenerator.writeEndObject();
        }
        */
    }
}