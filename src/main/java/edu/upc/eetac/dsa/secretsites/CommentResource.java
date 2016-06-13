package edu.upc.eetac.dsa.secretsites;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upc.eetac.dsa.secretsites.dao.CommentDAO;
import edu.upc.eetac.dsa.secretsites.dao.CommentDAOImpl;
import edu.upc.eetac.dsa.secretsites.entity.AuthToken;
import edu.upc.eetac.dsa.secretsites.entity.Comment;
import edu.upc.eetac.dsa.secretsites.entity.CommentCollection;
import jdk.nashorn.internal.parser.JSONParser;

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
    public CommentCollection getCommentsByInterestPointId(@PathParam("pointid") String pointid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before){
        CommentCollection commentCollection = null;
        CommentDAO commentDAO = new CommentDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            commentCollection = commentDAO.getCommentsByInterestPointId(pointid, timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return commentCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(SecretSitesMediaType.SECRETSITES_COMMENT)
    public Response getComment(@PathParam("id") String id, @Context Request request){
        // Create cache-control
        CacheControl cacheControl = new CacheControl();
        Comment comment = null;
        CommentDAO commentDAO = new CommentDAOImpl();
        try {
            comment = commentDAO.getCommentById(id);
            if(comment == null)
                throw new NotFoundException("Comment with id = "+id+" doesn't exist");
            // Calculate the ETag on last modified date of user resource
            EntityTag eTag = new EntityTag(Long.toString(comment.getLastModified()));
            // Verify if it matched with etag available in http request
            Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);
            // If ETag matches the rb will be non-null;
            // Use the rb to return the response without any further processing
            if (rb != null) {
                return rb.cacheControl(cacheControl).tag(eTag).build();
            }
            // If rb is null then either it is first time request; or resource is
            // modified
            // Get the updated representation and return with Etag attached to it
            rb = Response.ok(comment).cacheControl(cacheControl).tag(eTag);
            return rb.build();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    @Path("/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(SecretSitesMediaType.SECRETSITES_COMMENT)
    public Comment updateComment(@PathParam("id") String id, @FormParam("commentid") String commentid,
                                 @FormParam("userid") String userid, @FormParam("text") String text) {
        Comment comment = new Comment(commentid, userid, text);
        if(comment == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(comment.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String useridToken = securityContext.getUserPrincipal().getName();
        if(!useridToken.equals(comment.getUserid()))
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
    public Boolean deleteComment(@PathParam("id") String id) {
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
            return true;
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
