package edu.upc.eetac.dsa.secretsites;

import edu.upc.eetac.dsa.secretsites.dao.PhotoDAO;
import edu.upc.eetac.dsa.secretsites.dao.PhotoDAOImpl;
import edu.upc.eetac.dsa.secretsites.entity.AuthToken;
import edu.upc.eetac.dsa.secretsites.entity.Photo;
import edu.upc.eetac.dsa.secretsites.entity.PhotoCollection;
import javassist.*;

import javax.ws.rs.*;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Marti on 27/03/2016.
 */
@Path("photos")
public class PhotoResource {

    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(SecretSitesMediaType.SECRETSITES_PHOTO)
    public Response uploadPhoto(@FormParam("pointid") String pointid, @Context UriInfo uriInfo) throws URISyntaxException {
        if(pointid == null)
            throw new BadRequestException("all parameters are mandatory");
        PhotoDAO photoDAO = new PhotoDAOImpl();
        Photo photo = null;
        AuthToken authenticationToken = null;
        try {
            photo = photoDAO.uploadPhoto(pointid, securityContext.getUserPrincipal().getName());
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        //TODO REALLY: SAVE THE PHOTO IN MEMORY AS JPG (PUT IN PATH)
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + photo.getId());
        return Response.created(uri).type(SecretSitesMediaType.SECRETSITES_PHOTO).entity(photo).build();
    }

    @Path("interestpoint/{pointid}")
    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_PHOTO_COLLECTION)
    public PhotoCollection getPhotosByPointId(@PathParam("pointid") String pointid){
        PhotoCollection photoCollection = null;
        PhotoDAO photoDAO = new PhotoDAOImpl();
        try {
            photoCollection = photoDAO.getPhotosByPointId(pointid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return photoCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_PHOTO)
    public Photo getPhoto(@PathParam("id") String id){
        Photo photo = null;
        PhotoDAO photoDAO = new PhotoDAOImpl();
        try {
            photo = photoDAO.getPhotoById(id);
            if(photo == null)
                throw new NotFoundException("Photo with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return photo;
    }

    @Path("/{id}")
    @DELETE
    public void deletePhoto(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        PhotoDAO photoDAO = new PhotoDAOImpl();
        try {
            Photo photo = photoDAO.getPhotoById(id);
            if(photo == null)
                throw new NotFoundException("Photo with id = "+id+" doesn't exist");
            if(!userid.equals(photo.getUserid()))
                throw new ForbiddenException("operation not allowed");
            photoDAO.deletePhoto(id);
            //TODO REALLY:  ADMINISTRATOR CAN UPDATE TOO
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(SecretSitesMediaType.SECRETSITES_PHOTO)
    public Photo setPhotoRating(@FormParam("id") String id, @FormParam("rating") float rating, @Context UriInfo uriInfo) throws URISyntaxException {
        Photo photo = null;
        if(id == null || Float.isNaN(rating))
            throw new BadRequestException("all parameters are mandatory");
        PhotoDAO photoDAO = new PhotoDAOImpl();
        try {
            photo = photoDAO.setPhotoRating(id, securityContext.getUserPrincipal().getName(), rating);
            if(photo == null)
                throw new NotFoundException("Photo with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return photo;
    }

    @Path("rating/{id}")
    @DELETE
    public void deletePhotoRating(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        PhotoDAO photoDAO = new PhotoDAOImpl();
        try {
            if(Double.isNaN(photoDAO.getPhotoUserRating(id, userid))) //TODO Correct way to catch the error?
                throw new NotFoundException("Photo rating with photoid = "+id+" and userid = "+userid+" doesn't exist");
            photoDAO.deletePhotoRating(id, userid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

}
