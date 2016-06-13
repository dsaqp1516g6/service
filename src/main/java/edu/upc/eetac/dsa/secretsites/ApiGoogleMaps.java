package edu.upc.eetac.dsa.secretsites;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upc.eetac.dsa.secretsites.entity.Area;

import javax.ws.rs.PathParam;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by Marti on 05/04/2016.
 */
public class ApiGoogleMaps {

    public Area getGeocoding(String searchName) {
        Area area = new Area();
        JsonNode geometry = null;
        searchName = searchName.replace(" ", "%20");
        PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("googleAPIs");
        String geocodingUrl = prb.getString("googleData.geocodingUrl");
        String key = prb.getString("googleData.APIKey");
        String url = geocodingUrl + searchName + "&key=" + key;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResult = mapper.readTree(con.getInputStream());
            if ((responseCode == 200) && (jsonResult.get("status").toString().contains("OK"))) {
                JsonNode northeast = jsonResult.get("results").get(0).get("geometry").get("viewport").get("northeast");
                JsonNode southwest = jsonResult.get("results").get(0).get("geometry").get("viewport").get("southwest");
                JsonNode location = jsonResult.get("results").get(0).get("geometry").get("location");
                area.setCenter(location);
                area.setNorthEast(northeast);
                area.setSouthWest(southwest);
                return area;
            } else if(jsonResult.get("status").toString().contains("ZERO")) {
                //"No existe ninguna concidencia";
            }
            else {
                System.out.println("Error, responseCode = " + responseCode);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371; // Radius of the earth in km
        double dLat = (lat2 - lat1) * (Math.PI/180);
        double dLon = (lon2 - lon1) * (Math.PI/180);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * (Math.PI/180)) * Math.cos(lat2 * (Math.PI/180)) *
                Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        System.out.println("Distance in km: " + d);
        return d;
    }

    public double getLatLonFromDistanceInKm(double distance, double lat1, double lon1) {
        int R = 6371; // Radius of the earth in km

        double alpha = ((lat1 * R)* (Math.PI/180) - distance) / R;
        double beta = ((lon1 * R)* (Math.PI/180) - distance) / R;
        System.out.println("Latitude: " + alpha * (180 / Math.PI) + " Longitude: " + beta * (180 / Math.PI));
        return alpha;
        /*double c = distance / R;
        double a = Math.pow(Math.tan(c/2), 2)/(1 + Math.pow(Math.tan(c/2), 2));*/

    }

}
