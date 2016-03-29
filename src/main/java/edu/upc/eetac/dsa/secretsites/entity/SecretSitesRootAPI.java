package edu.upc.eetac.dsa.secretsites.entity;

import edu.upc.eetac.dsa.secretsites.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by Marti on 29/03/2016.
 */
public class SecretSitesRootAPI {
    @InjectLinks({
            @InjectLink(resource = SecretSitesRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "self bookmark home", title = "SecretSites Root API"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "login", title = "Login",  type= SecretSitesMediaType.SECRETSITES_AUTH_TOKEN),
            @InjectLink(resource = InterestPointResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-points", title = "Current points", type= SecretSitesMediaType.SECRETSITES_POINT_COLLECTION),
            @InjectLink(resource = UserResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-user", title = "Register", type= SecretSitesMediaType.SECRETSITES_AUTH_TOKEN),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout", condition="${!empty resource.userid}"),
            @InjectLink(resource = InterestPointResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-point", title = "Create point", condition="${!empty resource.userid}", type=SecretSitesMediaType.SECRETSITES_POINT),
            @InjectLink(resource = UserResource.class, method="getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", condition="${!empty resource.userid}", type= SecretSitesMediaType.SECRETSITES_USER, bindings = @Binding(name = "id", value = "${resource.userid}"))
    })
    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
