package model;

import java.util.Objects;
import java.util.UUID;

public class AuthData {
    private final String authToken;
    private final String username;

    public AuthData(String username) {
        this.username = username;
        this.authToken = UUID.randomUUID().toString();
    }

    public AuthData(String username, String token){
        this.username = username;
        this.authToken = token;
    }

    public String getUsername() {
        return this.username;
    }

    public String getToken() {
        return this.authToken;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AuthData authData = (AuthData)o;
            return Objects.equals(this.authToken, authData.authToken) && Objects.equals(this.username, authData.username);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.authToken, this.username});
    }

    public String toString() {
        return "AuthData{authToken='" + this.authToken + "', userName='" + this.username + "'}";
    }
}
