package dataaccess;

import model.AuthData;

public interface AuthDAO {

  public AuthData addAuthToken(String username) throws DataAccessException;
  public AuthData findToken(String token) throws DataAccessException;
  public void removeAuthToken(AuthData token) throws DataAccessException;
  public void clearAllAuth() throws DataAccessException;

}
