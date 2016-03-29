package edu.upc.eetac.dsa.secretsites;

import edu.upc.eetac.dsa.secretsites.entity.SecretSitesRootAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by Marti on 29/03/2016.
 */
@Path("/")
public class SecretSitesRootAPIResource {
    @Context
    private SecurityContext securityContext;

    private String userid;

    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_ROOT)
    public SecretSitesRootAPI getRootAPI() {
        if(securityContext.getUserPrincipal()!=null)
            userid = securityContext.getUserPrincipal().getName();
        SecretSitesRootAPI secretSitesRootAPI = new SecretSitesRootAPI();
        return secretSitesRootAPI;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
