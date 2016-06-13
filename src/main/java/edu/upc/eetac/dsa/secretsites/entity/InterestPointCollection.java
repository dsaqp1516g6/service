package edu.upc.eetac.dsa.secretsites.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.secretsites.InterestPointResource;
import edu.upc.eetac.dsa.secretsites.LoginResource;
import edu.upc.eetac.dsa.secretsites.SecretSitesMediaType;
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
public class InterestPointCollection {
    @InjectLinks({
            @InjectLink(resource = SecretSitesRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = " SecretSites Root API"),
            @InjectLink(resource = InterestPointResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-points", title = "Current points", type= SecretSitesMediaType.SECRETSITES_POINT_COLLECTION),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout")
    })
    private List<Link> links;
    private long newestTimestamp;
    private long oldestTimestamp;
    private List<InterestPoint> interestPoints = new ArrayList<>();
    private Area pointsArea;

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

    public List<InterestPoint> getInterestPoints() {
        return interestPoints;
    }

    public void setInterestPoints(List<InterestPoint> interestPoints) {
        this.interestPoints = interestPoints;
    }

    public Area getPointsArea() {
        return pointsArea;
    }

    public void setPointsArea(Area pointsArea) {
        this.pointsArea = pointsArea;
    }
}
