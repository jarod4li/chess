package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class UserDataDAO implements UserDAO {
    private final ArrayList<UserData> userList = new ArrayList<>();

    @Override
    public UserData getUserWithUsername(String username) {
        for (UserData user : userList) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserData getUserWithEmail(String email) {
        for (UserData user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void addUser(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        userList.add(user);
    }

    @Override
    public void clearAllUsers() {
        userList.clear();
    }
}
