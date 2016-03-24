package edu.upc.eetac.dsa.secretsites.entity;

import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by Marti on 24/03/2016.
 */
public class InterestPoint {
    @InjectLinks({})
    private List<Link> links;
    private String id;
    private String name;
    private double longitude;
    private double latitude;
    private Photo betterPhoto; //TODO is the correct whay to save the better photo?
    private float rating; //TODO IS CORRECT?
    private long creationTimestamp;
    //TODO LAST MODIFIED NECESSARY? (NO UPDATES)

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Photo getBetterPhoto() {
        return betterPhoto;
    }

    public void setBetterPhoto(Photo betterPhoto) {
        this.betterPhoto = betterPhoto;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

}
