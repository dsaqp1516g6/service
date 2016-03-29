package edu.upc.eetac.dsa.secretsites;

import edu.upc.eetac.dsa.secretsites.dao.AuthTokenDAO;
import edu.upc.eetac.dsa.secretsites.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.secretsites.dao.UserDAO;
import edu.upc.eetac.dsa.secretsites.dao.UserDAOImpl;
import edu.upc.eetac.dsa.secretsites.entity.AuthToken;
import edu.upc.eetac.dsa.secretsites.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.sql.SQLException;

/**
 * Created by Marti on 29/03/2016.
 */
@Path("login")
public class LoginResource {
    @Context
    SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(SecretSitesMediaType.SECRETSITES_AUTH_TOKEN)
    public AuthToken login(@FormParam("username") String username, @FormParam("password") String password) {
        if(username == null || password == null)
            throw new BadRequestException("all parameters are mandatory");

        User user = null;
        AuthToken authToken = null;
        try{
            UserDAO userDAO = new UserDAOImpl();
            user = userDAO.getUserByUsername(username);
            if(user == null)
                throw new BadRequestException("username " + username + " not found.");
            if(!userDAO.checkPassword(user.getId(), password))
                throw new BadRequestException("incorrect password");

            AuthTokenDAO authTokenDAO = new AuthTokenDAOImpl();
            authTokenDAO.deleteToken(user.getId());
            authToken = authTokenDAO.createAuthToken(user.getId());
        }catch(SQLException e){
            throw new InternalServerErrorException();
        }
        return authToken;
    }

    @DELETE
    public void logout(){
        String userid = securityContext.getUserPrincipal().getName();
        AuthTokenDAO authTokenDAO = new AuthTokenDAOImpl();
        try {
            authTokenDAO.deleteToken(userid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}