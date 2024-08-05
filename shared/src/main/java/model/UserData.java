package model;

import java.util.Objects;

public class UserData {

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
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, email);
  }

  @Override
  public String toString() {
    return "UserData{" +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            '}';
  }
}
