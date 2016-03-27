package edu.upc.eetac.dsa.secretsites.dao;

import edu.upc.eetac.dsa.secretsites.auth.UserInfo;
import edu.upc.eetac.dsa.secretsites.entity.AuthToken;

import java.sql.SQLException;

/**
 * Created by Marti on 24/03/2016.
 */
public interface AuthTokenDAO {
    public UserInfo getUserByAuthToken(String token) throws SQLException;
    public AuthToken createAuthToken(String userid) throws SQLException;
    public void deleteToken(String userid) throws  SQLException;
}
