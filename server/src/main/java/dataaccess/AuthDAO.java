package dataaccess;

import model.AuthData;
import java.util.ArrayList;

public interface AuthDAO {
    AuthData addAuthToken(String username) throws DataAccessException;
    AuthData findToken(String token) throws DataAccessException;
    void removeAuthToken(AuthData token) throws DataAccessException;
    void clearAllAuth() throws DataAccessException;
}

