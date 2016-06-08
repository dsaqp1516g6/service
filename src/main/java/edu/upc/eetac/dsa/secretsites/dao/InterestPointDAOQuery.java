package edu.upc.eetac.dsa.secretsites.dao;

/**
 * Created by Marti on 24/03/2016.
 */
public interface InterestPointDAOQuery {
    public final static String CREATE_POINT = "insert into interestpoints (id, name, longitude, latitude) values (UNHEX(?), ?, ?, ?)";
    public final static String GET_POINT_BY_ID = "select hex(id) as id, name, longitude, latitude, creation_timestamp from interestpoints where id=unhex(?)";
    public final static String GET_POINTS_BY_NAME = "select hex(id) as id, name, longitude, latitude, creation_timestamp from interestpoints where name LIKE ?";
    public final static String GET_POINTS = "select hex(id) as id, name, longitude, latitude, creation_timestamp from interestpoints";
    public final static String UPDATE_POINT = "update interestpoints set name=?, longitude=?, latitude=? where id=unhex(?) ";
    public final static String DELETE_POINT = "delete from interestpoints where id=unhex(?)";

    public final static String GET_POINT_STATUS = "select hex(pointid) as id, hex(userid) as userid, status from user_points where pointid=unhex(?) and userid=unhex(?)";
    public final static String GET_POINT_RATING = "select hex(pointid) as id, hex(userid) as userid, rating from rating_points where pointid=unhex(?)";
    public final static String GET_POINTS_BY_STATUS = "select hex(i.id) as id, i.name, i.longitude, i.latitude, i.creation_timestamp from interestpoints i, user_points u where " +
            "i.id=u.pointid and u.userid=unhex(?) and u.status=?";


    public final static String SET_POINT_RATING = "insert into rating_points (pointid, userid, rating) values (unhex(?), unhex(?), ?);";
    public final static String UPDATE_POINT_RATING = "update rating_points set rating=? where pointid=unhex(?) and userid=unhex(?);";
    public final static String GET_POINT_RATING_USER = "select hex(pointid) as id, hex(userid) as userid, rating from rating_points where pointid=unhex(?) and userid=unhex(?)";
    public final static String DELETE_POINT_RATING = "delete from rating_points where pointid=unhex(?) and userid=unhex(?);";


    public final static String SET_POINT_STATUS = "insert into user_points (pointid, userid, status) values (unhex(?), unhex(?), ?);";
    public final static String UPDATE_POINT_STATUS = "update user_points set status=? where pointid=unhex(?) and userid=unhex(?);";
    public final static String DELETE_POINT_STATUS = "delete from user_points where pointid=unhex(?) and userid=unhex(?);";

}
