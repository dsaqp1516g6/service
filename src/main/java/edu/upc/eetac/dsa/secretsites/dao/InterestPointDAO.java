package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.entity.InterestPoint;
import edu.upc.eetac.dsa.secretsites.entity.InterestPointCollection;

import java.sql.SQLException;

/**
 * Created by Marti on 24/03/2016.
 */
public interface InterestPointDAO {
    //TODO There is another way to get my userid always
    public InterestPoint createInterestPoint(String name, double longitude, double latitude) throws SQLException;
    public InterestPoint getInterestPointById(String id, String userid) throws SQLException;
    public InterestPointCollection getInterestPointsByStatus(String userid, String status) throws SQLException;
    public InterestPointCollection getInterestPoints(String userid) throws SQLException;
    public InterestPoint updateInterestPoint(String id, String userid, String name, double longitude, double latitude) throws SQLException;
    public boolean deleteInterestPoint(String id) throws SQLException;

    public double getInterestPointUserRating(String id, String userid) throws SQLException;
    public String getInterestPointStatus(String id, String userid) throws SQLException;
    public InterestPoint setInterestPointRating(String id, String userid, float rating) throws SQLException;
    public InterestPoint setInterestPointStatus(String id, String userid, String status) throws SQLException;
    public boolean deleteInterestPointRating(String id, String userid) throws SQLException;
    public boolean deleteInterestPointStatus(String id, String userid) throws SQLException;

}
