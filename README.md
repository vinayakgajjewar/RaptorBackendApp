# RaptorBackendApp
Backend for my UCR DAF 2021 research project under Dr. Ahmed Eldawy

Raptor is a Raster+Vector query processing engine designed to efficiently combine raster data, like satellite images, with vector data, like roads and boundaries in one efficient query processing core. Raptor has already been applied to estimating crop yield and combating wildfires.

The zonal statistics query takes in an aggregate function (sum, avg, max), a set of geometries (state/county boundaries), and a raster file (temperature data). The query computes the aggregate functions for each geometry on the raster data. For example, it can compute the average temperature for each state.

## Usage
Use ```> mvn jetty:run``` to build and run the web application without needing to assemble it into a WAR file.

## Building a WAR file
Use ```> mvn package``` to generate a WAR file which can be deployed to a production server.