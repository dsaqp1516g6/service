package edu.upc.eetac.dsa.secretsites.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.secretsites.LoginResource;
import edu.upc.eetac.dsa.secretsites.PhotoResource;
import edu.upc.eetac.dsa.secretsites.SecretSitesRootAPIResource;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marti on 24/03/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoCollection {
    /*@InjectLinks({
            @InjectLink(resource = SecretSitesRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "SecretSites Root API"),
            @InjectLink(resource = PhotoResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-photos", title = "Current photos"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout")
    })*/
    @InjectLinks({})
    private List<Link> links;
    private long newestTimestamp;
    private long oldestTimestamp;
    private List<Photo> photos = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public long getNewestTimestamp() {
        return newestTimestamp;
    }

    public void setNewestTimestamp(long newestTimestamp) {
        this.newestTimestamp = newestTimestamp;
    }

    public long getOldestTimestamp() {
        return oldestTimestamp;
    }

    public void setOldestTimestamp(long oldestTimestamp) {
        this.oldestTimestamp = oldestTimestamp;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
