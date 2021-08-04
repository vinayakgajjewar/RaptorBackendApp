# Raptor

Backend for my UCR DAF 2021 research project under Dr. Ahmed Eldawy

Raptor is a Raster+Vector query processing engine designed to efficiently combine raster data, like satellite images, with vector data, like roads and boundaries in one efficient query processing core. Raptor has already been applied to estimating crop yield and combating wildfires.

## Zonal statistics

The zonal statistics query takes in an aggregate function (sum, avg, max, ...), a set of geometries (state/county boundaries), and a raster file (temperature data). The query computes the aggregate functions for each geometry on the raster data. For example, it can compute the average temperature for each state.

## Usage

### Setup

This project requires Java 8 to work correctly with Apache Spark.

In addition, you will need a MongoDB connection string to be able to connect to a cluster. It should look something like the following.

```mongodb+srv://<some_user>:<some_passwd>@freecluster.ts1sn.mongodb.net/test?retryWrites=true&w=majority```

### Development

Use ```> mvn jetty:run -Dmongodb.uri="<my_connection_string>"``` to build and run the web application without needing to assemble it into a WAR file.

### Building a WAR file

Use ```> mvn package``` to generate a WAR file which can be deployed to a production server.