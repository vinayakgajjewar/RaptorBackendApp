package org.bdlabucr;

import java.io.IOException;

import java.util.*; // maps

import java.nio.file.Paths; // paths

// jackson library to read/write json files
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RaptorServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        //response.getWriter().println("{}");
        try (JsonGenerator jsonGenerator = new JsonFactory().createGenerator(response.getOutputStream())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("field1");
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("num1", 3);
            jsonGenerator.writeEndObject();
            jsonGenerator.writeEndObject();
            /*ObjectMapper mapper = new ObjectMapper();
            Map<?, ?> map = mapper.readValue(Paths.get("TIGER2018_ZCTA5_riverside.geojson").toFile(), Map.class);*/
        }
    }
}