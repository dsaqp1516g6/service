package edu.upc.eetac.dsa.secretsites;

import edu.upc.eetac.dsa.secretsites.dao.InterestPointDAO;
import edu.upc.eetac.dsa.secretsites.dao.InterestPointDAOImpl;
import edu.upc.eetac.dsa.secretsites.entity.InterestPointCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.sql.SQLException;

/**
 * Created by Marti on 13/06/2016.
 */
@Path("search")
public class SearchResource {

    @Context
    private SecurityContext securityContext;

    @Path("/{name}")
    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_POINT_COLLECTION)
    public InterestPointCollection getInterestPointByNameAndArea(@PathParam("name") String name) {
        InterestPointCollection pointCollection = null;
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        Principal principal = securityContext.getUserPrincipal();
        try {
            if(principal == null)
                pointCollection = pointDAO.getInterestPointsByName("Unknown", name);
            else
                pointCollection = pointDAO.getInterestPointsByName(principal.getName(), name);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return pointCollection;
    }
}
