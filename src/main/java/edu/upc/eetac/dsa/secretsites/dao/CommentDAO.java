package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.entity.Comment;
import edu.upc.eetac.dsa.secretsites.entity.CommentCollection;

import java.sql.SQLException;

/**
 * Created by Marti on 26/03/2016.
 */
public interface CommentDAO {
    public Comment createComment(String pointid, String userid, String text) throws SQLException;
    public Comment getCommentById(String id) throws SQLException;
    public CommentCollection getCommentsByInterestPointId(String pointid, long timestamp, boolean before) throws SQLException;
    public Comment updateComment(String id, String text) throws SQLException;
    public boolean deleteComment(String id) throws SQLException;
}
