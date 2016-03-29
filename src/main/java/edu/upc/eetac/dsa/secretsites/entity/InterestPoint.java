package edu.upc.eetac.dsa.secretsites.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by Marti on 24/03/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterestPoint {
    @InjectLinks({})
    private List<Link> links;
    private CommentCollection comments;
    private PhotoCollection photos;
    private String id;
    private String name;
    private double longitude;
    private double latitude;
    private Photo bestPhoto; //TODO is the correct whay to save the better photo?
    private float rating; //TODO IS CORRECT?
    private String status; //TODO AND THIS ONE?
    private long creationTimestamp;
    //TODO LAST MODIFIED NECESSARY? (NO UPDATES)


    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public CommentCollection getComments() {
        return comments;
    }

    public void setComments(CommentCollection comments) {
        this.comments = comments;
    }

    public PhotoCollection getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoCollection photos) {
        this.photos = photos;
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

    public Photo getBestPhoto() {
        return bestPhoto;
    }

    public void setBestPhoto(Photo bestPhoto) {
        this.bestPhoto = bestPhoto;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

}
