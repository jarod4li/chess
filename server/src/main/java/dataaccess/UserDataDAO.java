package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class UserDataDAO implements UserDAO {
  private ArrayList<UserData> userList = new ArrayList<>();

  public UserData getUserWithUsername(String username){
    for (UserData user:userList) {
      if (user.getName().equals(username)){
        return user;
      }
    }
    return null;
  }
  public UserData getUserWithEmail(String email){
    for (UserData user:userList) {
      if (user.getEmail().equals(email)){
        return user;
      }
    }
    return null;
  }

  public void addUser(String username, String password, String email){
    UserData user = new UserData(username, password, email);
    userList.add(user);
  }

  public void clearAllUsers(){
    userList.clear();
  }
}
