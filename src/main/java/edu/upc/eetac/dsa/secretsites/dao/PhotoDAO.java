package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.entity.Photo;
import edu.upc.eetac.dsa.secretsites.entity.PhotoCollection;

import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by Marti on 26/03/2016.
 */
public interface PhotoDAO {
    public Photo uploadPhoto(String pointid, String userid, InputStream image) throws SQLException;
    public Photo getPhotoById(String id) throws SQLException;
    public PhotoCollection getPhotosByPointId(String pointid) throws SQLException;
    public boolean deletePhoto(String id) throws SQLException;

    public Photo getBestVotedPhoto(PhotoCollection photos) throws SQLException;
    public double getPhotoUserRating(String id, String userid) throws SQLException;
    public Photo setPhotoRating(String id, String userid, float rating) throws SQLException;
    public boolean deletePhotoRating(String id, String userid) throws SQLException;
}
