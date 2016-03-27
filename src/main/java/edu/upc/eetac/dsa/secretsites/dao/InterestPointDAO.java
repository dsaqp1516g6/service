package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.entity.InterestPoint;
import edu.upc.eetac.dsa.secretsites.entity.InterestPointCollection;

import java.sql.SQLException;

/**
 * Created by Marti on 24/03/2016.
 */
public interface InterestPointDAO {
    public InterestPoint createInterestPoint(String name, double longitude, double latitude) throws SQLException;
    public InterestPoint getInterestPointById(String id, String userid) throws SQLException;
    //TODO There is another way to get the my userid always (For visit or pendent points)
    public InterestPointCollection getInterestPoints(String userid) throws SQLException;
    public InterestPoint updateInterestPoint(String id, String userid, String name, double longitude, double latitude) throws SQLException;  //TODO ONLY ADMIN (NAME AND COORDENATES)
    public boolean deleteInterestPoint(String id) throws SQLException; //TODO ONLY ADMIN
}
