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
import edu.ucr.cs.bdlab.beast.JavaSpatialRDDHelper;


import org.apache.spark.api.java.JavaRDD;
import org.apache.hadoop.conf.Configuration;
import org.apache.spark.api.java.JavaSparkContext;
// https://spark.apache.org/docs/latest/rdd-programming-guide.html
//import org.apache.spark.SparkContext;
//import org.apache.spark.SparkConf;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class RaptorServlet extends HttpServlet {

    protected SparkConnector sparkconnector;

    protected JavaSpatialSparkContext jssc;



    protected DBRead dbr;

    public RaptorServlet() {
        System.out.println("----initializing servlet");

        // initialize DB reader
        dbr = DBRead.getInstance();

        // get or create spark context
        sparkconnector = SparkConnector.getInstance();

        jssc = new JavaSpatialSparkContext(sparkconnector.getSC());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        float minx = Float.parseFloat(request.getParameter("minx"));
        float miny = Float.parseFloat(request.getParameter("miny"));
        float maxx = Float.parseFloat(request.getParameter("maxx"));
        float maxy = Float.parseFloat(request.getParameter("maxy"));

        dbr.read();

        // we set content-type as application/geo+json
        // not application/json
        response.setContentType("application/geo+json");
        response.setStatus(HttpServletResponse.SC_OK);

        // set Access-Control-Allow-Origin
        // otherwise, the front-end won't be able to make GET requests to this server
        // because of CORS policy
        response.addHeader("Access-Control-Allow-Origin", "*");

        // read an example geojson file into a list

        JavaRDD<IFeature> records = jssc.readCSVPoint("data/csv/test_wildfire_visualization_4326.csv", "x", "y", '\t', true);

        // filter by map extents
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry extents = geometryFactory.toGeometry(new Envelope(minx, miny, maxx, maxy));
        List<IFeature> filteredRecords = JavaSpatialRDDHelper.rangeQuery(records, extents).collect();

        //List<IFeature> records = SpatialReader.readInput(sparkconnector.getSC(), new BeastOptions(), "data/geojson/TIGER2018_STATE_data_index.geojson", "geojson").collect();
        //JavaRDD<IFeature> records = SpatialReader.readInput(sc, new BeastOptions(), "exampleinput.geojson", "geojson");

        System.out.println("----done reading records");

        // try writing out a record
        try (GeoJSONFeatureWriter writer = new GeoJSONFeatureWriter()) {
            writer.initialize(response.getOutputStream(), new Configuration());
            for (int i = 0; i < filteredRecords.size(); i++) {
                writer.write(filteredRecords.get(i));
            }
            //writer.write(records.get(0));
            //writer.write(records.first());
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}