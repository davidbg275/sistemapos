package models;

import java.sql.*;

public class AuthRepositoryImpl implements AuthRepository {

  // ---------- ONLINE (Supabase / PostgreSQL) ----------
  @Override
  public Usuario loginOnline(String username, String password) throws SQLException {
    String sql = """
            select username, rol, activo
            from public.usuarios
            where username = ? and password = ?
            limit 1
        """;

    try (Connection pg = DriverManager.getConnection(
        SupabaseConfig.DB_URL, SupabaseConfig.DB_USER, SupabaseConfig.DB_PASS);
        PreparedStatement ps = pg.prepareStatement(sql)) {

      ps.setString(1, username);
      ps.setString(2, password);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String u = rs.getString("username");
          String rol = rs.getString("rol");
          boolean activo = rs.getBoolean("activo");
          return new Usuario(u, rol, activo);
        }
        return null; // credenciales incorrectas
      }
    }
    // Si falla la red se propagará SQLException (para que el caller caiga a
    // offline)
  }

  // ---------- OFFLINE (SQLite) ----------
  @Override
  public Usuario loginOffline(String username, String password) throws SQLException {
    String sql = """
            select username, rol, activo
            from usuarios_local
            where username = ? and password = ? and activo = 1
            limit 1
        """;

    try (Connection cx = DatabaseLocal.connect();
        PreparedStatement ps = cx.prepareStatement(sql)) {

      ps.setString(1, username);
      ps.setString(2, password);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String u = rs.getString("username");
          String rol = rs.getString("rol");
          boolean activo = rs.getInt("activo") == 1;
          return new Usuario(u, rol, activo);
        }
        return null;
      }
    }
  }

  // ---------- PULL (FULL REFRESH sencillo) ----------
  @Override
  public void pullAllUsersFromSupabase() throws SQLException {
    // 1) Leer todos los usuarios de Supabase
    String qSupabase = """
            select id, username, password, rol, activo
            from public.usuarios
        """;

    // 2) Preparar SQLite: borrar y volver a insertar (para pocos usuarios es
    // perfecto)
    String deleteLocal = "delete from usuarios_local";
    String insertLocal = """
            insert into usuarios_local(id, username, password, rol, activo)
            values (?,?,?,?,?)
        """;

    try (Connection pg = DriverManager.getConnection(
        SupabaseConfig.DB_URL, SupabaseConfig.DB_USER, SupabaseConfig.DB_PASS);
        PreparedStatement psPg = pg.prepareStatement(qSupabase);
        ResultSet rs = psPg.executeQuery();
        Connection sl = DatabaseLocal.connect();
        PreparedStatement del = sl.prepareStatement(deleteLocal);
        PreparedStatement ins = sl.prepareStatement(insertLocal)) {

      sl.setAutoCommit(false); // transacción para mayor seguridad

      // limpiar tabla local
      del.executeUpdate();

      // insertar todos los usuarios
      while (rs.next()) {
        ins.setString(1, rs.getString("id"));
        ins.setString(2, rs.getString("username"));
        ins.setString(3, rs.getString("password")); // texto (solo para el ejercicio)
        ins.setString(4, rs.getString("rol"));
        ins.setInt(5, rs.getBoolean("activo") ? 1 : 0); // boolean PG -> int SQLite
        ins.addBatch();
      }

      ins.executeBatch();
      sl.commit();
      System.out.println("✅ Pull completado: usuarios locales actualizados.");
    }
  }
}
