package edu.upc.eetac.dsa.secretsites;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    private static String baseURI;

    public final static String getBaseURI() {
        if (baseURI == null) {
            PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("secretsites");
            baseURI = prb.getString("secretSites.context");
        }
        return baseURI;
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in edu.upc.eetac.dsa.secretsites package
        final ResourceConfig rc = new SecretSitesResourceConfig();

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(getBaseURI()), rc);
        //HttpHandler httpHandler = new CLStaticHttpHandler(HttpServer.class.getClassLoader(), "/web/");
        HttpHandler httpHandler = new StaticHttpHandler("./img/");
        httpServer.getServerConfiguration().addHttpHandler(httpHandler, "/");

        for (NetworkListener l : httpServer.getListeners()) {
            l.getFileCache().setEnabled(false);
        }

        return httpServer;

    }

    //TODO Paginació per photos (i points si vols)
    //TODO Els links estant tots? Faltan segur

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        new ApiGoogleMaps().getGeocoding("Barcelona");

        /*new ApiGoogleMaps().getDistanceFromLatLonInKm(41.562786, 0.968573, 41.594081, 1.340561);
        new ApiGoogleMaps().getLatLonFromDistanceInKm(31.136680618795065, 41.562786, 0.968573);*/


        /*new ApiGoogleMaps().getDistanceFromLatLonInKm(41.3870194, 2.167858417, 41.37352957591122, 2.154368592911219);
        new ApiGoogleMaps().getLatLonFromDistanceInKm(1.5, 41.3870194, 2.167858417);

        System.in.read();
        server.shutdownNow();*/
    }



    //Save Photo INFO:
    //PHOTO ID: 8F559094F5DC11E5B12940167E1246AC
    //POINT ID: 0E05ECC9F4D911E5B12940167E1246AC
    //Point Name: London

    /* Photos from london
        +----------------------------------+----------------------------------+
        | hex(id)                          | hex(pointid)                     |
        +----------------------------------+----------------------------------+
        | 0E791036F4D911E5B12940167E1246AC | 0E05ECC9F4D911E5B12940167E1246AC |
        | 0EB63472F4D911E5B12940167E1246AC | 0E05ECC9F4D911E5B12940167E1246AC |
        | 8F559094F5DC11E5B12940167E1246AC | 0E05ECC9F4D911E5B12940167E1246AC |
        | A4EEE5AEF5DA11E5B12940167E1246AC | 0E05ECC9F4D911E5B12940167E1246AC |
        +----------------------------------+----------------------------------+

     */
}

