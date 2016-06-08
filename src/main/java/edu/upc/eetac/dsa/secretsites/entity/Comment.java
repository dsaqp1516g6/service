package edu.upc.eetac.dsa.secretsites.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.secretsites.CommentResource;
import edu.upc.eetac.dsa.secretsites.LoginResource;
import edu.upc.eetac.dsa.secretsites.SecretSitesRootAPIResource;
import edu.upc.eetac.dsa.secretsites.UserResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Marti on 24/03/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment {
    /*@InjectLinks({
            @InjectLink(resource = SecretSitesRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "SecretSites Root API"),
            @InjectLink(resource = CommentResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-comments", title = "Current comments"),
            @InjectLink(resource = CommentResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-comment", title = "Create comment", type = MediaType.APPLICATION_FORM_URLENCODED),
            @InjectLink(resource = CommentResource.class, method = "getComment", style = InjectLink.Style.ABSOLUTE, rel = "self Comment", title = "Comment", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = UserResource.class, method = "getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", bindings = @Binding(name = "id", value = "${instance.userid}")),
            @InjectLink(resource = CommentResource.class, method = "getCommentsByInterestPointId", style = InjectLink.Style.ABSOLUTE, rel = "next", title = "Newer comments", bindings = {@Binding(name = "pointid", value = "${instance.pointid}"), @Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "false")}),
            @InjectLink(resource = CommentResource.class, method = "getCommentsByInterestPointId", style = InjectLink.Style.ABSOLUTE, rel = "previous", title = "Older comments", bindings = {@Binding(name = "pointid", value = "${instance.pointid}"), @Binding(name = "timestamp", value = "${instance.creationTimestamp}"), @Binding(name = "before", value = "true")})
    })*/
    @InjectLinks({})
    private List<Link> links;
    private String id;
    private String pointid;
    private String userid;
    private String username;
    private String text;
    private long creationTimestamp;
    private long lastModified;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}

