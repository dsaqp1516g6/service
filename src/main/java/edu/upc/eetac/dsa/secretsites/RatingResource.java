package edu.upc.eetac.dsa.secretsites;

import edu.upc.eetac.dsa.secretsites.SecretSitesMediaType;
import edu.upc.eetac.dsa.secretsites.dao.PhotoDAO;
import edu.upc.eetac.dsa.secretsites.dao.PhotoDAOImpl;
import edu.upc.eetac.dsa.secretsites.entity.Photo;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Marti on 12/06/2016.
 */

@Path("rating")
public class RatingResource {

    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(SecretSitesMediaType.SECRETSITES_PHOTO)
    public Photo setPhotoRating(@FormParam("id") String id, @FormParam("rating") String rating, @Context UriInfo uriInfo) throws URISyntaxException {
        Photo photo = null;
        if(id == null || rating == null)
            throw new BadRequestException("all parameters are mandatory");
        float frating = Float.parseFloat(rating);
        PhotoDAO photoDAO = new PhotoDAOImpl();
        try {
            photo = photoDAO.setPhotoRating(id, securityContext.getUserPrincipal().getName(), frating);
            if(photo == null)
                throw new NotFoundException("Photo with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return photo;
    }

    @Path("/{id}")
    @DELETE
    public Boolean deletePhotoRating(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        PhotoDAO photoDAO = new PhotoDAOImpl();
        try {
            if(Float.isNaN(photoDAO.getPhotoUserRating(id, userid))) //TODO Correct way to catch the error?
                throw new NotFoundException("Photo rating with photoid = "+id+" and userid = "+userid+" doesn't exist");
            photoDAO.deletePhotoRating(id, userid);
            return true;
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
