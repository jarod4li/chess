package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthDataDAO implements AuthDAO {
  private ArrayList<AuthData> tokenList = new ArrayList<>();

  public AuthData addAuthToken(String username){
    AuthData newUser = new AuthData(username);
    tokenList.add(newUser);
    return newUser;
  }

  public AuthData findToken(String token){
    for (AuthData authToken:tokenList) {
      if (authToken.getToken().equals(token)){
        return authToken;
      }
    }
    return null;
  }

  public void removeAuthToken(AuthData token){
    tokenList.remove(token);
  }

  public void clearAllAuth() {
    tokenList.clear();
  }
}
