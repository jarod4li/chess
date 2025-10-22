package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthDataDAO implements AuthDAO {
    private final ArrayList<AuthData> tokenList = new ArrayList<>();

    @Override
    public AuthData addAuthToken(String username) {
        AuthData newUser = new AuthData(username);
        tokenList.add(newUser);
        return newUser;
    }

    @Override
    public AuthData findToken(String token) {
        for (AuthData authToken : tokenList) {
            if (authToken.getToken().equals(token)) {
                return authToken;
            }
        }
        return null;
    }

    @Override
    public void removeAuthToken(AuthData token) {
        tokenList.remove(token);
    }

    @Override
    public void clearAllAuth() {
        tokenList.clear();
    }
}
