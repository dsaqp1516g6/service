package edu.upc.eetac.dsa.secretsites;

import edu.upc.eetac.dsa.secretsites.dao.PhotoDAO;
import edu.upc.eetac.dsa.secretsites.dao.PhotoDAOImpl;
import edu.upc.eetac.dsa.secretsites.entity.AuthToken;
import edu.upc.eetac.dsa.secretsites.entity.Photo;
import edu.upc.eetac.dsa.secretsites.entity.PhotoCollection;
import javassist.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Marti on 27/03/2016.
 */
@Path("photos")
public class PhotoResource {

    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(SecretSitesMediaType.SECRETSITES_PHOTO)
    public Response uploadPhoto(@FormDataParam("pointid") String pointid, @FormDataParam("image") InputStream image, @FormDataParam("image") FormDataContentDisposition fileDisposition, @Context UriInfo uriInfo) throws URISyntaxException {
        if(pointid == null)
            throw new BadRequestException("all parameters are mandatory");
        PhotoDAO photoDAO = new PhotoDAOImpl();
        Photo photo = null;
        AuthToken authenticationToken = null;
        try {
            photo = photoDAO.uploadPhoto(pointid, securityContext.getUserPrincipal().getName(), image);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + photo.getId());
        return Response.created(uri).type(SecretSitesMediaType.SECRETSITES_PHOTO).entity(photo).build();
    }

    @Path("interestpoint/{pointid}")
    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_PHOTO_COLLECTION)
    public PhotoCollection getPhotosByPointId(@PathParam("pointid") String pointid){
        PhotoCollection photoCollection = null;
        String userid = securityContext.getUserPrincipal().getName();
        PhotoDAO photoDAO = new PhotoDAOImpl();
        try {
            photoCollection = photoDAO.getPhotosByPointId(pointid, userid);
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
        Principal principal = securityContext.getUserPrincipal();
        try {
            if(principal == null)
                photo = photoDAO.getPhotoById(id, "Unknow");
            else
                photo = photoDAO.getPhotoById(id, securityContext.getUserPrincipal().getName());
            if(photo == null)
                throw new NotFoundException("Photo with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return photo;
    }

    @Path("/{id}")
    @DELETE
    public Boolean deletePhoto(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        PhotoDAO photoDAO = new PhotoDAOImpl();
        try {
            Photo photo = photoDAO.getPhotoById(id, userid);
            if(photo == null)
                throw new NotFoundException("Photo with id = "+id+" doesn't exist");
            if(!userid.equals(photo.getUserid()))
                throw new ForbiddenException("operation not allowed");
            photoDAO.deletePhoto(id);
            return true;
            //TODO REALLY:  ADMINISTRATOR CAN UPDATE TOO
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

}
