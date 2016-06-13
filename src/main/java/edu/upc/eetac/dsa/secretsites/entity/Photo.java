package edu.upc.eetac.dsa.secretsites.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.secretsites.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Marti on 24/03/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Photo {
    @InjectLinks({
            @InjectLink(resource = SecretSitesRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "SecretSites Root API"),
            @InjectLink(resource = PhotoResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-photos", title = "Current photos"),
            @InjectLink(resource = PhotoResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-photo", title = "Create photo", type = MediaType.APPLICATION_FORM_URLENCODED),
            @InjectLink(resource = PhotoResource.class, method = "getPhoto", style = InjectLink.Style.ABSOLUTE, rel = "self-photo", title = "Photo", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = RatingResource.class, style = InjectLink.Style.ABSOLUTE, rel = "rating-photo", title = "Rating Photo", bindings = @Binding(name = "id", value = "${instance.id}")),
    })
    private List<Link> links;
    private String id;
    private String pointid;
    private String userid;
    private String username;
    private String url;
    private float myRating;
    private float totalRating;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getMyRating() {
        return myRating;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(float totalRating) {
        this.totalRating = totalRating;
    }

    public long getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(long uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }
}
