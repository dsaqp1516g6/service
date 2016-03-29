package edu.upc.eetac.dsa.secretsites.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.secretsites.CommentResource;
import edu.upc.eetac.dsa.secretsites.LoginResource;
import edu.upc.eetac.dsa.secretsites.SecretSitesRootAPIResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marti on 24/03/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentCollection {
    /*@InjectLinks({
            @InjectLink(resource = SecretSitesRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "SecretSites Root API"),
            @InjectLink(resource = CommentResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-comments", title = "Current comments"),
            @InjectLink(resource = CommentResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-comments", title = "Current comments"),
            //TODO FIX IT! Next and previous
            //@InjectLink(resource = CommentResource.class, method = "getCommentsByInterestPointId", style = InjectLink.Style.ABSOLUTE, rel = "next", title = "Newer comments", bindings = {@Binding(name = "pointid", value = "${instance.pointid}"), @Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "false")}),
            //@InjectLink(resource = CommentResource.class, method = "getCommentsByInterestPointId", style = InjectLink.Style.ABSOLUTE, rel = "previous", title = "Older comments", bindings = {@Binding(name = "pointid", value = "${instance.pointid}"), @Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "true")}),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout")
    })*/
    @InjectLinks({})
    private List<Link> links;
    private long newestTimestamp;
    private long oldestTimestamp;
    private List<Comment> comments = new ArrayList<>();

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
