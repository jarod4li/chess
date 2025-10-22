package model;

import java.util.Objects;

public class UserData {
    private final String username;
    private final String password;
    private final String email;

    public UserData(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // New getter that the server expects
    public String getUsername() {
        return username;
    }

    // Keep old getter if you want
    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}

        UserData user = (UserData) o;

        if (!Objects.equals(username, user.username)) {return false;}
        if (!Objects.equals(password, user.password)) {return false;}
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
