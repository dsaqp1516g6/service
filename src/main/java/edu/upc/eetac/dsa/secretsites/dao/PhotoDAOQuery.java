package edu.upc.eetac.dsa.secretsites.dao;

/**
 * Created by Marti on 27/03/2016.
 */
public interface PhotoDAOQuery {
    public final static String UPLOAD_PHOTO = "insert into photos (id, pointid, userid, upload_timestamp) values (UNHEX(?), unhex(?), unhex(?), ?)";
    public final static String GET_PHOTO_BY_ID = "select hex(id) as id, hex(pointid) as pointid, hex(userid) as userid, upload_timestamp from photos where id=unhex(?)";
    public final static String GET_PHOTOS_BY_POINT_ID = "select hex(id) as id, hex(pointid) as pointid, hex(userid) as userid, upload_timestamp from photos where pointid=unhex(?)";
    public final static String DELETE_PHOTO = "delete from photos where id=unhex(?)";

    public final static String GET_RATING_PHOTO = "select hex(p.id) as id, hex(p.pointid) as pointid, hex(p.userid) as userid, p.upload_timestamp, r.rating from photos p, rating_photos r " +
            "where pointid=unhex(?) and p.id=r.photoid ORDER BY r.rating DESC limit 1";
    public final static String GET_PHOTO_RATING = "select hex(r.photoid) as id, hex(r.userid) as userid, r.rating from rating_photos r, photos p where p.pointid=unhex(?) " +
            "and r.photoid=p.id";

}
