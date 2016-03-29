package edu.upc.eetac.dsa.secretsites.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.secretsites.LoginResource;
import edu.upc.eetac.dsa.secretsites.PhotoResource;
import edu.upc.eetac.dsa.secretsites.SecretSitesRootAPIResource;
import edu.upc.eetac.dsa.secretsites.UserResource;
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
    //TODO IS THIS CLASS CORRECT?
    /*@InjectLinks({
            @InjectLink(resource = SecretSitesRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "SecretSites Root API"),
            @InjectLink(resource = PhotoResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-photos", title = "Current photos"),
            @InjectLink(resource = PhotoResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-photo", title = "Create photo", type = MediaType.APPLICATION_FORM_URLENCODED),
            @InjectLink(resource = PhotoResource.class, method = "getPhoto", style = InjectLink.Style.ABSOLUTE, rel = "self photo", title = "Photo", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = UserResource.class, method = "getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", bindings = @Binding(name = "id", value = "${instance.userid}")),
    })*/
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
