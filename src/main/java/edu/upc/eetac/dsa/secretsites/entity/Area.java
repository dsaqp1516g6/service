package edu.upc.eetac.dsa.secretsites.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Marti on 12/06/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Area {

    private float longCenter;
    private float latCenter;
    private float longNorth;
    private float latNorth;
    private float longSouth;
    private float latSouth;

    public float getLongCenter() {
        return longCenter;
    }

    public void setLongCenter(float longCenter) {
        this.longCenter = longCenter;
    }

    public float getLatCenter() {
        return latCenter;
    }

    public void setLatCenter(float latCenter) {
        this.latCenter = latCenter;
    }

    public float getLongNorth() {
        return longNorth;
    }

    public void setLongNorth(float longNorth) {
        this.longNorth = longNorth;
    }

    public float getLatNorth() {
        return latNorth;
    }

    public void setLatNorth(float latNorth) {
        this.latNorth = latNorth;
    }

    public float getLongSouth() {
        return longSouth;
    }

    public void setLongSouth(float longSouth) {
        this.longSouth = longSouth;
    }

    public float getLatSouth() {
        return latSouth;
    }

    public void setLatSouth(float latSouth) {
        this.latSouth = latSouth;
    }

    public void setCenter(JsonNode center) {
        this.latCenter = (float) center.get("lat").asDouble();
        this.longCenter = (float) center.get("lng").asDouble();
    }

    public void setNorthEast(JsonNode north) {
        this.latNorth = (float) north.get("lat").asDouble();
        this.longNorth = (float) north.get("lng").asDouble();
    }

    public void setSouthWest(JsonNode south) {
        this.latSouth = (float) south.get("lat").asDouble();
        this.longSouth = (float) south.get("lng").asDouble();
    }
}
