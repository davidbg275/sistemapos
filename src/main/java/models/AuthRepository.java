package models;

import java.sql.SQLException;

public interface AuthRepository {
  Usuario loginOnline(String username, String password) throws SQLException;

  Usuario loginOffline(String username, String password) throws SQLException;

  void pullAllUsersFromSupabase() throws SQLException;
}
