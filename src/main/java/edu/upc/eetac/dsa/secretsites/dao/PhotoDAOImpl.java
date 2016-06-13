package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.entity.Photo;
import edu.upc.eetac.dsa.secretsites.entity.PhotoCollection;

import javax.imageio.ImageIO;
import javax.ws.rs.InternalServerErrorException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Marti on 27/03/2016.
 */
public class PhotoDAOImpl implements PhotoDAO {
    @Override
    public Photo uploadPhoto(String pointid, String userid, InputStream image) throws SQLException {
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
            uploadImageToServer(image, id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getPhotoById(id, userid);
    }

    @Override
    public Photo getPhotoById(String id, String userid) throws SQLException {
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
                photo.setUsername(rs.getString("username"));
                photo.setUploadTimestamp((rs.getTimestamp("upload_timestamp").getTime()));
                photo.setTotalRating((new InterestPointDAOImpl()).getRating(photo.getId(), false));
                photo.setMyRating(getPhotoUserRating(photo.getId(), userid));
                photo.setUrl(photo.getId().toLowerCase() + ".png");
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
    public PhotoCollection getPhotosByPointId(String pointid, String userid) throws SQLException {
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
                photo.setUsername(rs.getString("username"));
                photo.setUploadTimestamp((rs.getTimestamp("upload_timestamp").getTime()));
                photo.setTotalRating((new InterestPointDAOImpl()).getRating(photo.getId(), false));
                photo.setMyRating(getPhotoUserRating(photo.getId(), userid));
                photo.setUrl(photo.getId().toLowerCase() + ".png");
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
    public Photo getBestVotedPhoto(PhotoCollection photos) throws SQLException {
        Collections.sort(photos.getPhotos(), new Comparator<Photo>() {
            @Override
            public int compare(Photo lhs, Photo rhs) {
                return lhs.getTotalRating() > rhs.getTotalRating() ? -1 : (lhs.getTotalRating() > rhs.getTotalRating()) ? 1 : 0;
            }
        });
        return (photos.getPhotos().size() != 0) ? photos.getPhotos().get(0) : null;
    }

    @Override
    public Photo setPhotoRating(String id, String userid, float rating) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            if(Float.isNaN(getPhotoUserRating(id, userid))) {
                stmt = connection.prepareStatement(PhotoDAOQuery.SET_PHOTO_RATING);
                stmt.setString(1, id);
                stmt.setString(2, userid);
                stmt.setFloat(3, rating);
            }
            else {
                stmt = connection.prepareStatement(PhotoDAOQuery.UPDATE_PHOTO_RATING);
                stmt.setFloat(1, rating);
                stmt.setString(2, id);
                stmt.setString(3, userid);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return getPhotoById(id, userid);
    }

    @Override
    public boolean deletePhotoRating(String id, String userid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(PhotoDAOQuery.DELETE_PHOTO_RATING);
            stmt.setString(1, id);
            stmt.setString(2, userid);

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
    public float getPhotoUserRating(String id, String userid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(PhotoDAOQuery.GET_PHOTO_RATING_USER);
            stmt.setString(1, id);
            stmt.setString(2, userid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("rating");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return Float.NaN;
    }


    private boolean uploadImageToServer(InputStream file, String id) {
        BufferedImage image = null;
        BufferedImage imageResized = null;
        try {
            image = ImageIO.read(file);
            //Resize image for less KB
            imageResized = resize(image, image.getWidth() / 2, image.getHeight() / 2);
        } catch (IOException e) {
            throw new InternalServerErrorException("Something has been wrong when reading the file.");
        }
        String filename = "img\\" + id.toString() + ".png";
        try {
            //ImageIO.write(image, "jpg", new File(filename));
            ImageIO.write(imageResized, "png", new File(filename));
        } catch (IOException e) {
            throw new InternalServerErrorException("Something has been wrong when converting the file.");
        }
        return true;
    }

    private BufferedImage resize(BufferedImage original, int newWidth, int newHeight) {
        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(),
                original.getHeight(), null);
        g.dispose();
        return resized;
    }
}
