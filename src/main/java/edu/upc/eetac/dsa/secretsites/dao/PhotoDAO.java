package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.entity.Photo;
import edu.upc.eetac.dsa.secretsites.entity.PhotoCollection;

import java.sql.SQLException;

/**
 * Created by Marti on 26/03/2016.
 */
public interface PhotoDAO {
    public Photo uploadPhoto(String pointid, String userid) throws SQLException;
    public Photo getPhotoById(String id) throws SQLException;
    public PhotoCollection getPhotosByPointId(String pointid) throws SQLException;
    public boolean deletePhoto(String id) throws SQLException;

    public Photo getBestVotedPhotoByPointId(String pointid) throws SQLException;
}
