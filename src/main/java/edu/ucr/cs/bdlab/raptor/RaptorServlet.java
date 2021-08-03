//package org.bdlabucr;
package edu.ucr.cs.bdlab.raptor;

import java.io.IOException;

// import java.util.*; // maps
import java.util.List; // lists

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // we set content-type as application/geo+json
        // not application/json
        response.setContentType("application/geo+json");
        response.setStatus(HttpServletResponse.SC_OK);

        // create our JavaSparkContext
        SparkConf sparkconf = new SparkConf().setAppName("appName").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(sparkconf);

        // read an example geojson file into a list
        List<IFeature> records = SpatialReader.readInput(sc, new BeastOptions(), "exampleinput.geojson", "geojson").collect();

        try (GeoJSONFeatureWriter writer = new GeoJSONFeatureWriter()) {
            writer.initialize(response.getOutputStream(), new Configuration());
            //System.out.println("SIZEE:");
            //System.out.println(records.size());
            writer.write(records.get(0));
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}