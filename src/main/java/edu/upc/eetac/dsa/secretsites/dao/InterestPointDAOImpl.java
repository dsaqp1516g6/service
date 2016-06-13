package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.ApiGoogleMaps;
import edu.upc.eetac.dsa.secretsites.entity.Area;
import edu.upc.eetac.dsa.secretsites.entity.InterestPoint;
import edu.upc.eetac.dsa.secretsites.entity.InterestPointCollection;
import edu.upc.eetac.dsa.secretsites.entity.PhotoCollection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * Created by Marti on 24/03/2016.
 */
public class InterestPointDAOImpl implements InterestPointDAO {
    @Override
    public InterestPoint createInterestPoint(String name, double longitude, double latitude, String description) throws SQLException {
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

            stmt = connection.prepareStatement(InterestPointDAOQuery.CREATE_POINT);
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setDouble(3, longitude);
            stmt.setDouble(4, latitude);
            stmt.setString(5, description);
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
        return getInterestPointById(id, "Unknown");
    }

    @Override
    public InterestPoint getInterestPointById(String id, String userid) throws SQLException {
        InterestPoint point = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(InterestPointDAOQuery.GET_POINT_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                point = new InterestPoint();
                point.setId(rs.getString("id"));
                point.setName(rs.getString("name"));
                point.setDescription(rs.getString("description"));
                point.setLongitude(rs.getDouble("longitude"));
                point.setLatitude(rs.getDouble("latitude"));
                point.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                point.setStatus(getInterestPointStatus(id, userid));
                point.setRating(getRating(id, true));
                point.setComments((new CommentDAOImpl()).getCommentsByInterestPointId(id, System.currentTimeMillis(), true));
                point.setPhotos((new PhotoDAOImpl()).getPhotosByPointId(id, userid));  //TODO ORDER BY SOMETHING?¿
                point.setBestPhoto((new PhotoDAOImpl()).getBestVotedPhoto(point.getPhotos())); //TODO to get the best voted photo in a point
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return point;
    }

    //TODO (REALLY) getInterestPointsByArea
    @Override
    public InterestPointCollection getInterestPoints(String userid) throws SQLException {
        InterestPointCollection pointCollection = new InterestPointCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(InterestPointDAOQuery.GET_POINTS);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                InterestPoint point = new InterestPoint();
                point.setId(rs.getString("id"));
                point.setName(rs.getString("name"));
                point.setDescription(rs.getString("description"));
                point.setLongitude(rs.getDouble("longitude"));
                point.setLatitude(rs.getDouble("latitude"));
                point.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                point.setStatus(getInterestPointStatus(point.getId(), userid));
                point.setRating(getRating(point.getId(), true));
                PhotoCollection photosCollection = (new PhotoDAOImpl()).getPhotosByPointId(point.getId(), userid);  //TODO ORDER BY SOMETHING?¿
                point.setBestPhoto((new PhotoDAOImpl()).getBestVotedPhoto(photosCollection));
                if (first) {
                    pointCollection.setNewestTimestamp(point.getCreationTimestamp());
                    first = false;
                }
                pointCollection.setOldestTimestamp(point.getCreationTimestamp());
                pointCollection.getInterestPoints().add(point);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return pointCollection;
    }

    @Override
    public InterestPointCollection getInterestPointsByName(String userid, String searchName) throws SQLException {
        float littleLng;
        float littleLat;
        float bigLng;
        float bigLat;
        InterestPointCollection pointCollection = new InterestPointCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(InterestPointDAOQuery.GET_POINTS_BY_NAME);
            stmt.setString(1, "%" + searchName + "%");

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                InterestPoint point = new InterestPoint();
                point.setId(rs.getString("id"));
                point.setName(rs.getString("name"));
                point.setDescription(rs.getString("description"));
                point.setLatitude(rs.getDouble("latitude"));
                point.setLongitude(rs.getDouble("longitude"));
                point.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                point.setStatus(getInterestPointStatus(point.getId(), userid));
                point.setRating(getRating(point.getId(), true));
                PhotoCollection photosCollection = (new PhotoDAOImpl()).getPhotosByPointId(point.getId(), userid);  //TODO ORDER BY SOMETHING?¿
                point.setBestPhoto((new PhotoDAOImpl()).getBestVotedPhoto(photosCollection));
                if (first) {
                    pointCollection.setNewestTimestamp(point.getCreationTimestamp());
                    first = false;
                }
                pointCollection.setOldestTimestamp(point.getCreationTimestamp());
                pointCollection.getInterestPoints().add(point);
            }
            Area area = (new ApiGoogleMaps()).getGeocoding(searchName);
            if(area != null) {
                pointCollection.setPointsArea(area);
                if (area.getLongSouth() < area.getLongNorth()) {
                    littleLng = area.getLongSouth();
                    bigLng = area.getLongNorth();
                } else {
                    littleLng = area.getLongNorth();
                    bigLng = area.getLongSouth();
                }
                if (area.getLatSouth() < area.getLatNorth()) {
                    littleLat = area.getLatSouth();
                    bigLat = area.getLatNorth();
                } else {
                    littleLat = area.getLatNorth();
                    bigLat = area.getLatSouth();
                }
                pointCollection = getInterestPointsByLatLng(userid,
                        littleLat, bigLat, littleLng, bigLng, pointCollection);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return pointCollection;
    }

    public InterestPointCollection getInterestPointsByLatLng(String userid, float littleLat, float bigLat,
                                                             float littleLng, float bigLng, InterestPointCollection pointCollection) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(InterestPointDAOQuery.GET_POINTS_BY_LATLNG);
            stmt.setFloat(1, bigLng);
            stmt.setFloat(2, littleLng);
            stmt.setFloat(3, bigLat);
            stmt.setFloat(4, littleLat);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                InterestPoint point = new InterestPoint();
                point.setId(rs.getString("id"));
                point.setName(rs.getString("name"));
                point.setDescription(rs.getString("description"));
                point.setLatitude(rs.getDouble("latitude"));
                point.setLongitude(rs.getDouble("longitude"));
                point.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                point.setStatus(getInterestPointStatus(point.getId(), userid));
                point.setRating(getRating(point.getId(), true));
                PhotoCollection photosCollection = (new PhotoDAOImpl()).getPhotosByPointId(point.getId(), userid);
                point.setBestPhoto((new PhotoDAOImpl()).getBestVotedPhoto(photosCollection));
                pointCollection.setOldestTimestamp(point.getCreationTimestamp());
                pointCollection.getInterestPoints().add(point);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return pointCollection;
    }

    @Override
    public InterestPointCollection getInterestPointsByStatus(String userid, String status) throws SQLException {
        InterestPointCollection pointCollection = new InterestPointCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(InterestPointDAOQuery.GET_POINTS_BY_STATUS);
            stmt.setString(1, userid);
            stmt.setString(2, status);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                InterestPoint point = new InterestPoint();
                point.setId(rs.getString("id"));
                point.setName(rs.getString("name"));
                point.setDescription(rs.getString("description"));
                point.setLongitude(rs.getDouble("longitude"));
                point.setLatitude(rs.getDouble("latitude"));
                point.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                point.setStatus(status);
                point.setRating(getRating(point.getId(), true));
                PhotoCollection photosCollection = (new PhotoDAOImpl()).getPhotosByPointId(point.getId(), userid);  //TODO ORDER BY SOMETHING?¿
                point.setBestPhoto((new PhotoDAOImpl()).getBestVotedPhoto(photosCollection));
                if (first) {
                    pointCollection.setNewestTimestamp(point.getCreationTimestamp());
                    first = false;
                }
                pointCollection.setOldestTimestamp(point.getCreationTimestamp());
                pointCollection.getInterestPoints().add(point);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return pointCollection;
    }

    @Override
    public InterestPoint updateInterestPoint(String id, String userid, String name, double longitude, double latitude, String description) throws SQLException {
        InterestPoint point = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(InterestPointDAOQuery.UPDATE_POINT);
            stmt.setString(1, name);
            stmt.setDouble(2, longitude);
            stmt.setDouble(3, latitude);
            stmt.setString(4, id);
            stmt.setString(5, description);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                point = getInterestPointById(id, userid);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return point;
    }

    @Override
    public boolean deleteInterestPoint(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(InterestPointDAOQuery.DELETE_POINT);
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
    public InterestPoint setInterestPointRating(String id, String userid, float rating) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            if(Double.isNaN(getInterestPointUserRating(id, userid))) {
                stmt = connection.prepareStatement(InterestPointDAOQuery.SET_POINT_RATING);
                stmt.setString(1, id);
                stmt.setString(2, userid);
                stmt.setFloat(3, rating);
            }
            else {
                stmt = connection.prepareStatement(InterestPointDAOQuery.UPDATE_POINT_RATING);
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
        return getInterestPointById(id, userid);
    }

    @Override
    public boolean deleteInterestPointRating(String id, String userid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(InterestPointDAOQuery.DELETE_POINT_RATING);
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
    public InterestPoint setInterestPointStatus(String id, String userid, String status) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            if(getInterestPointStatus(id, userid).compareTo("Unknown") == 0) {
                stmt = connection.prepareStatement(InterestPointDAOQuery.SET_POINT_STATUS);
                stmt.setString(1, id);
                stmt.setString(2, userid);
                stmt.setString(3, status);
            }
            else {
                stmt = connection.prepareStatement(InterestPointDAOQuery.UPDATE_POINT_STATUS);
                stmt.setString(1, status);
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
        return getInterestPointById(id, userid);
    }

    @Override
    public boolean deleteInterestPointStatus(String id, String userid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(InterestPointDAOQuery.DELETE_POINT_STATUS);
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
    public String getInterestPointStatus(String id, String userid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(InterestPointDAOQuery.GET_POINT_STATUS);
            stmt.setString(1, id);
            stmt.setString(2, userid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return "Unknown";
    }

    @Override
    public double getInterestPointUserRating(String id, String userid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(InterestPointDAOQuery.GET_POINT_RATING_USER);
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
        return Double.NaN;
    }


    public float getRating(String id, boolean isPoint) throws SQLException {
        int numOfRatings = 0;
        float totalRatingValue = 0;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            if(isPoint)
                stmt = connection.prepareStatement(InterestPointDAOQuery.GET_POINT_RATING);
            else
                stmt = connection.prepareStatement(PhotoDAOQuery.GET_PHOTO_RATING);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                totalRatingValue += rs.getFloat("rating");
                numOfRatings++;
            }
            if(numOfRatings != 0) {
                BigDecimal bd = new BigDecimal(Float.toString(totalRatingValue / numOfRatings));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                return bd.floatValue();
            }
            return 0.00f;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}
