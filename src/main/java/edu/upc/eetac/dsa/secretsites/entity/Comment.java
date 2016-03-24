package edu.upc.eetac.dsa.secretsites.entity;

import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by Marti on 24/03/2016.
 */
public class Comment {
    @InjectLinks({})
    private List<Link> links;
    private String id;
    private String pointid;
    private String userid;
    private String text;
    private long creationTimestamp;
    private long lastModified;
}
