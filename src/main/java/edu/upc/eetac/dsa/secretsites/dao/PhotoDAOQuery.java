package edu.upc.eetac.dsa.secretsites.dao;

/**
 * Created by Marti on 27/03/2016.
 */
public interface PhotoDAOQuery {
    public final static String UPLOAD_PHOTO = "insert into photos (id, pointid, userid) values (UNHEX(?), unhex(?), unhex(?))";
    public final static String GET_PHOTO_BY_ID = "select hex(photos.id) as id, hex(pointid) as pointid, hex(userid) as userid, upload_timestamp, users.username as username from photos join users " +
            "on photos.userid = users.id where id=unhex(?)";
    public final static String GET_PHOTOS_BY_POINT_ID = "select hex(photos.id) as id, hex(pointid) as pointid, hex(userid) as userid, upload_timestamp, users.username as username from photos join users " +
            "on photos.userid = users.id where pointid=unhex(?)";
    public final static String DELETE_PHOTO = "delete from photos where id=unhex(?)";

    public final static String GET_PHOTO_RATING = "select hex(photoid) as id, hex(userid) as userid, rating from rating_photos where photoid = unhex(?)";
    public final static String GET_PHOTO_RATING_USER = "select hex(photoid) as id, hex(userid) as userid, rating from rating_photos where photoid=unhex(?) and userid=unhex(?)";
    public final static String SET_PHOTO_RATING = "insert into rating_photos (photoid, userid, rating) values (unhex(?), unhex(?), ?);";
    public final static String UPDATE_PHOTO_RATING = "update rating_photos set rating=? where photoid=unhex(?) and userid=unhex(?);";
    public final static String DELETE_PHOTO_RATING = "delete from rating_photos where photoid=unhex(?) and userid=unhex(?);";
}
