package dataaccess;

import model.UserData;
import java.util.ArrayList;

public interface UserDAO {
    UserData getUserWithUsername(String username) throws DataAccessException;
    UserData getUserWithEmail(String email) throws DataAccessException;
    void addUser(String username, String password, String email) throws DataAccessException;
    void clearAllUsers() throws DataAccessException;
}

