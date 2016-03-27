package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.entity.InterestPoint;
import edu.upc.eetac.dsa.secretsites.entity.InterestPointCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Marti on 24/03/2016.
 */
public class InterestPointDAOImpl implements InterestPointDAO {
    @Override
    public InterestPoint createInterestPoint(String name, double longitude, double latitude) throws SQLException {
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
        return getInterestPointById(id, "NULL");
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
                point.setLongitude(rs.getDouble("longitude"));
                point.setLongitude(rs.getDouble("latitude"));
                point.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                point.setStatus(getInterestPointStatus(id, userid));
                point.setRating(getRating(id, true));
                point.setBestPhoto((new PhotoDAOImpl()).getBestVotedPhotoByPointId(id)); //TODO to get the best voted photo in a point
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return point;
    }

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
                point.setLongitude(rs.getDouble("longitude"));
                point.setLatitude(rs.getDouble("latitude"));
                point.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                point.setStatus(getInterestPointStatus(point.getId(), userid));
                point.setRating(getRating(point.getId(), true));
                point.setBestPhoto((new PhotoDAOImpl()).getBestVotedPhotoByPointId(point.getId())); //TODO to get the best voted photo in a point too
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
    //TODO (REALLY) getInterestPointsByArea, getInterestPointsByVisited, getInterestPointsByPendent

    @Override
    public InterestPoint updateInterestPoint(String id, String userid, String name, double longitude, double latitude) throws SQLException {
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


    public String getInterestPointStatus(String id, String userid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(InterestPointDAOQuery.GET_POINT_STATUS);
            stmt.setString(1, id);
            stmt.setString(1, userid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
            return "";
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
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
            return (totalRatingValue/numOfRatings);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}