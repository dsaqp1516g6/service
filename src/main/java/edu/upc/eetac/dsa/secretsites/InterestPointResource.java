package edu.upc.eetac.dsa.secretsites;

import edu.upc.eetac.dsa.secretsites.auth.UserInfo;
import edu.upc.eetac.dsa.secretsites.dao.InterestPointDAO;
import edu.upc.eetac.dsa.secretsites.dao.InterestPointDAOImpl;
import edu.upc.eetac.dsa.secretsites.entity.AuthToken;
import edu.upc.eetac.dsa.secretsites.entity.InterestPoint;
import edu.upc.eetac.dsa.secretsites.entity.InterestPointCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.sql.SQLException;

/**
 * Created by Marti on 27/03/2016.
 */
@Path("interestpoints")
public class InterestPointResource {

    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(SecretSitesMediaType.SECRETSITES_POINT)
    public Response createInterestPoint(@FormParam("name") String name, @FormParam("longitude") double longitude, @FormParam("latitude") double latitude, @Context UriInfo uriInfo) throws URISyntaxException {
        if(name == null || Double.isNaN(longitude) ||  Double.isNaN(latitude))
            throw new BadRequestException("all parameters are mandatory");
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        InterestPoint point = null;
        AuthToken authenticationToken = null;
        try {
            point = pointDAO.createInterestPoint(name, longitude, latitude);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + point.getId());
        return Response.created(uri).type(SecretSitesMediaType.SECRETSITES_POINT).entity(point).build();
    }

    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_POINT_COLLECTION)
    public InterestPointCollection getInterestPoints(){
        InterestPointCollection pointCollection = null;
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        Principal principal = securityContext.getUserPrincipal();
        try {
             if(principal == null)
                pointCollection = pointDAO.getInterestPoints("Unknown");
            else
                pointCollection = pointDAO.getInterestPoints(principal.getName());
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return pointCollection;
    }

    @Path("/search/{name}")
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

    @Path("/status/{status}")
    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_POINT_COLLECTION)
    public InterestPointCollection getInterestPointsByStatus(@PathParam("status") String status) {
        if(status.compareTo("visited") != 0 && status.compareTo("pendent") != 0)
            throw new BadRequestException("the parameter are wrong (only two status: visited or pendent)");
        InterestPointCollection pointCollection = null;
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        try {
            pointCollection = pointDAO.getInterestPointsByStatus(securityContext.getUserPrincipal().getName(), status);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return pointCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_POINT)
    public InterestPoint getInterestPoint(@PathParam("id") String id){
        InterestPoint point = null;
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        Principal principal = securityContext.getUserPrincipal();
        try {
            if(principal == null)
                point = pointDAO.getInterestPointById(id, "Unknown");
            else
                point = pointDAO.getInterestPointById(id, principal.getName());
            if(point == null)
                throw new NotFoundException("Interest point with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return point;
    }

    @Path("/{id}")
    @PUT
    @Consumes(SecretSitesMediaType.SECRETSITES_POINT)
    @Produces(SecretSitesMediaType.SECRETSITES_POINT)
    public InterestPoint updateInterestPoint(@PathParam("id") String id, InterestPoint point) {
        if(point == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(point.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        //TODO REALLY: ONLY THE ADMIN CAN UPDATE THE POINT

        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        try {
            point = pointDAO.updateInterestPoint(id, securityContext.getUserPrincipal().getName(), point.getName(), point.getLongitude(), point.getLatitude());
            if(point == null)
                throw new NotFoundException("Interest point with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return point;
    }

    @Path("/{id}")
    @DELETE
    public void deleteInterestPoint(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        try {
            //TODO REALLY: ONLY THE ADMIN CAN UPDATE THE POINT
            if(!pointDAO.deleteInterestPoint(id))
                throw new NotFoundException("Interest point with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(SecretSitesMediaType.SECRETSITES_POINT)
    public InterestPoint setInterestPointRating(@FormParam("id") String id, @FormParam("rating") float rating, @Context UriInfo uriInfo) throws URISyntaxException {
        InterestPoint point = null;
        if(id == null || Float.isNaN(rating))
            throw new BadRequestException("all parameters are mandatory");
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        try {
            point = pointDAO.setInterestPointRating(id, securityContext.getUserPrincipal().getName(), rating);
            if(point == null)
                throw new NotFoundException("Interest point with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return point;
    }

    @Path("rating/{id}")
    @DELETE
    public void deleteInterestPointRating(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        try {
            if(Double.isNaN(pointDAO.getInterestPointUserRating(id, userid))) //TODO Correct way to catch the error?
                throw new NotFoundException("Interest point rating with pointid = "+id+" and userid = "+userid+" doesn't exist");
            pointDAO.deleteInterestPointRating(id, userid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    @Path("status")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(SecretSitesMediaType.SECRETSITES_POINT)
    public InterestPoint setInterestPointStatus(@FormParam("id") String id, @FormParam("status") String status) {
        InterestPoint point = null;
        if(status.compareTo("visited") != 0 && status.compareTo("pendent") != 0)
            throw new BadRequestException("the parameter are wrong (only two status: visited or pendent)");
        if(id == null)
            throw new BadRequestException("all parameters are mandatory");
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        try {
            point = pointDAO.setInterestPointStatus(id, securityContext.getUserPrincipal().getName(), status);
            if(point == null)
                throw new NotFoundException("Interest point with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return point;
    }

    @Path("status/{id}")
    @DELETE
    public void deleteInterestPointStatus(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        InterestPointDAO pointDAO = new InterestPointDAOImpl();
        try {
            if(pointDAO.getInterestPointStatus(id, userid).compareTo("Unknown") == 0) //TODO Correct way to catch the error?
                throw new NotFoundException("Interest point status with pointid = "+id+" and userid = "+userid+" doesn't exist");
            pointDAO.deleteInterestPointStatus(id, userid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

}
