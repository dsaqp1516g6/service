package edu.upc.eetac.dsa.secretsites;

import edu.upc.eetac.dsa.secretsites.dao.CommentDAO;
import edu.upc.eetac.dsa.secretsites.dao.CommentDAOImpl;
import edu.upc.eetac.dsa.secretsites.entity.AuthToken;
import edu.upc.eetac.dsa.secretsites.entity.Comment;
import edu.upc.eetac.dsa.secretsites.entity.CommentCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Marti on 27/03/2016.
 */
@Path("comments")
public class CommentResource {

    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(SecretSitesMediaType.SECRETSITES_COMMENT)
    public Response createComment(@FormParam("pointid") String pointid, @FormParam("text") String text, @Context UriInfo uriInfo) throws URISyntaxException {
        if(pointid == null || text == null)
            throw new BadRequestException("all parameters are mandatory");
        CommentDAO commentDAO = new CommentDAOImpl();
        Comment comment = null;
        AuthToken authenticationToken = null;
        try {
            comment = commentDAO.createComment(pointid, securityContext.getUserPrincipal().getName(), text);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + comment.getId());
        return Response.created(uri).type(SecretSitesMediaType.SECRETSITES_COMMENT).entity(comment).build();
    }

    @Path("interestpoint/{pointid}")
    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_COMMENT_COLLECTION)
    public CommentCollection getCommentsByInterestPointId(@PathParam("pointid") String pointid){
        CommentCollection commentCollection = null;
        CommentDAO commentDAO = new CommentDAOImpl();
        try {
            commentCollection = commentDAO.getCommentsByInterestPointId(pointid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return commentCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_COMMENT)
    public Comment getComment(@PathParam("id") String id){
        Comment comment = null;
        CommentDAO commentDAO = new CommentDAOImpl();
        try {
            comment = commentDAO.getCommentById(id);
            if(comment == null)
                throw new NotFoundException("Comment with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return comment;
    }

    @Path("/{id}")
    @PUT
    @Consumes(SecretSitesMediaType.SECRETSITES_COMMENT)
    @Produces(SecretSitesMediaType.SECRETSITES_COMMENT)
    public Comment updateComment(@PathParam("id") String id, Comment comment) {
        if(comment == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(comment.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(comment.getUserid()))
            throw new ForbiddenException("operation not allowed");
        //TODO REALLY:  ADMINISTRATOR CAN UPDATE TOO

        CommentDAO commentDAO = new CommentDAOImpl();
        try {
            comment = commentDAO.updateComment(id, comment.getText());
            if(comment == null)
                throw new NotFoundException("Comment with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return comment;
    }

    @Path("/{id}")
    @DELETE
    public void deleteComment(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        CommentDAO commentDAO = new CommentDAOImpl();
        try {
            Comment comment = commentDAO.getCommentById(id);
            if(comment == null)
                throw new NotFoundException("Comment with id = "+id+" doesn't exist");
            if(!userid.equals(comment.getUserid()))
                throw new ForbiddenException("operation not allowed");
            //TODO REALLY:  ADMINISTRATOR CAN UPDATE TOO
            commentDAO.deleteComment(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
