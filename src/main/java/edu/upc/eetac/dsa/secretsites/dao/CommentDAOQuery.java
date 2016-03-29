package edu.upc.eetac.dsa.secretsites.dao;

/**
 * Created by Marti on 26/03/2016.
 */
public interface CommentDAOQuery {
    public final static String CREATE_COMMENT = "insert into comments (id, pointid, userid, text) values (unhex(?), unhex(?), unhex(?), ?)";
    public final static String GET_COMMENT_BY_ID = "select hex(id) as id, hex(pointid) as pointid, hex(userid) as userid, text, creation_timestamp, last_modified from comments where id=unhex(?)";
    public final static String UPDATE_COMMENT = "update comments set text=? where id=unhex(?) ";
    public final static String DELETE_COMMENT = "delete from comments where id=unhex(?)";

    //Paginació
    public final static String GET_COMMENTS_BY_POINT_ID = "select hex(id) as id, hex(pointid) as pointid, hex(userid) as userid, text, creation_timestamp, last_modified from comments where pointid=unhex(?) " +
            "and creation_timestamp < ? order by creation_timestamp desc limit 5";
    public final static String GET_COMMENTS_BY_POINT_ID_AFTER = "select hex(id) as id, hex(pointid) as pointid, hex(userid) as userid, text, creation_timestamp, last_modified from comments where pointid=unhex(?) " +
            "and creation_timestamp > ? order by creation_timestamp desc limit 5";



}
