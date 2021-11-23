# Raptor

Backend for my UCR DAF 2021 research project under Dr. Ahmed Eldawy

Raptor is a Raster+Vector query processing engine designed to efficiently combine raster data, like satellite images, with vector data, like roads and boundaries in one efficient query processing core. Raptor has already been applied to estimating crop yield and combating wildfires.

## Zonal statistics

The zonal statistics query takes in an aggregate function (sum, avg, max, ...), a set of geometries (state/county boundaries), and a raster file (temperature data). The query computes the aggregate functions for each geometry on the raster data. For example, it can compute the average temperature for each state.

## Usage

### Setup

This project requires Java 1.8.0 to work correctly with Apache Spark. Additionally, deploying to Apache Tomcat requires Tomcat 9.

In addition, you will need a MongoDB connection string to be able to connect to a cluster. It should look something like the following.

```mongodb+srv://<some_user>:<some_passwd>@freecluster.ts1sn.mongodb.net/test?retryWrites=true&w=majority```

### Development

Use ```> mvn jetty:run -Dmongodb.uri="<my_connection_string>"``` to build and run the web application without needing to assemble it into a WAR file.

### Building a WAR file

Use ```> mvn package``` to generate a WAR file which can be deployed to a production server.

# Deploying to Apache Tomcat

First, download Tomcat 9 from [this link](https://tomcat.apache.org/download-90.cgi). To deploy the generated WAR file, copy it to the /webapps directory inside of the Tomcat installation. To be able to start Apache Tomcat, you need to make `startup.sh`, `shutdown.sh`, and `catalina.sh` executable. Add the following lines to `conf/tomcat-users.xml`:

```xml
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<user username="admin" password="password" roles="manager-gui, manager-script"/>
```

Move the R-tree files to `apache-tomcat-9.0.54/data/rtree/wildfire_index/` and start the Tomcat server using `> ./bin/startup.sh`. It is important that the Tomcat server is started from inside the `apache-tomcat-9.0.54/` directory so that Tomcat knows where the data files for the web application are located.

Navigate to `localhost:8080/manager` and log in with your admin credentials to access the Tomcat Manager, where the Raptor web app should appear under Applications.