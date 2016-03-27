package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.entity.Photo;
import edu.upc.eetac.dsa.secretsites.entity.PhotoCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Marti on 27/03/2016.
 */
public class PhotoDAOImpl implements PhotoDAO {
    @Override
    public Photo uploadPhoto(String pointid, String userid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(PhotoDAOQuery.UPLOAD_PHOTO);
            stmt.setString(1, id);
            stmt.setString(2, pointid);
            stmt.setString(3, userid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getPhotoById(id);
    }

    @Override
    public Photo getPhotoById(String id) throws SQLException {
        Photo photo = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(PhotoDAOQuery.GET_PHOTO_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                photo = new Photo();
                photo.setId(rs.getString("id"));
                photo.setPointid(rs.getString("pointid"));
                photo.setUserid(rs.getString("userid"));
                photo.setUploadTimestamp(rs.getTimestamp("upload_timestamp").getTime());
                photo.setRating((new InterestPointDAOImpl()).getRating(id, false));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return photo;
    }

    @Override
    public PhotoCollection getPhotosByPointId(String pointid) throws SQLException {
        PhotoCollection photoCollection = new PhotoCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(PhotoDAOQuery.GET_PHOTOS_BY_POINT_ID);
            stmt.setString(1, pointid);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Photo photo = new Photo();
                photo.setId(rs.getString("id"));
                photo.setPointid(rs.getString("pointid"));
                photo.setUserid(rs.getString("userid"));
                photo.setUploadTimestamp((rs.getTimestamp("creation_timestamp").getTime()));
                photo.setRating((new InterestPointDAOImpl()).getRating(photo.getId(), false));
                if (first) {
                    photoCollection.setNewestTimestamp(photo.getUploadTimestamp());
                    first = false;
                }
                photoCollection.setOldestTimestamp(photo.getUploadTimestamp());
                photoCollection.getPhotos().add(photo);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return photoCollection;
    }

    @Override
    public boolean deletePhoto(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(PhotoDAOQuery.DELETE_PHOTO);
            stmt.setString(1, id);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public Photo getBestVotedPhotoByPointId(String pointid) throws SQLException {
        Photo photo = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(PhotoDAOQuery.GET_RATING_PHOTO);
            stmt.setString(1, pointid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                photo = new Photo();
                photo.setId(rs.getString("id"));
                photo.setPointid(rs.getString("pointid"));
                photo.setUserid(rs.getString("userid"));
                photo.setUploadTimestamp(rs.getTimestamp("upload_timestamp").getTime());
                photo.setRating(rs.getFloat("rating"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return photo;
    }
}
