package edu.upc.eetac.dsa.secretsites.entity;

import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Marti on 24/03/2016.
 */
public class Photo {
    //TODO IS THIS CLASS CORRECT?
    @InjectLinks({})
    private List<Link> links;
    private String id;
    private String pointid;
    private String userid;
    private BufferedImage photo;  //TODO Best way to save a image?
    private float rating; //TODO this okey too?
    private long uploadTimestamp;

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

    public String getPointid() {
        return pointid;
    }

    public void setPointid(String pointid) {
        this.pointid = pointid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public BufferedImage getPhoto() {
        return photo;
    }

    public void setPhoto(BufferedImage photo) {
        this.photo = photo;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(long uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }
}
