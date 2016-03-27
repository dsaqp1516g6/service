package edu.upc.eetac.dsa.secretsites.dao;

/**
 * Created by Marti on 24/03/2016.
 */
public interface InterestPointDAOQuery {
    public final static String CREATE_POINT = "insert into interestpoints (id, name, longitude, latitude) values (UNHEX(?), ?, ?, ?)";
    public final static String GET_POINT_BY_ID = "select hex(id) as id, name, longitude, latitude, creation_timestamp from interestpoints where id=unhex(?)";
    public final static String GET_POINTS = "select hex(id) as id, name, longitude, latitude, creation_timestamp from interestpoints";
    public final static String UPDATE_POINT = "update interestpoints set name=?, longitude=?, latitude=? where id=unhex(?) ";
    public final static String DELETE_POINT = "delete from interestpoints where id=unhex(?)";

    public final static String GET_POINT_STATUS = "select hex(pointid) as id, hex(userid) as userid, status from user_points where pointid=unhex(?) and userid=unhex(?)";
    public final static String GET_POINT_RATING = "select hex(pointid) as id, hex(userid) as userid, rating from rating_points where pointid=unhex(?)";
}
