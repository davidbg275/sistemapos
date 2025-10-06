package models;

import java.io.File;
import java.sql.*;

public class DatabaseLocal {

  // Cambia la ruta si lo prefieres (pero crea la carpeta 'data' antes).
  public static final String SQLITE_FILE = "/home/david/Documentos/java/papeleria/src/main/resources/data/papeleria.db";
  public static final String SQLITE_URL = "jdbc:sqlite:" + SQLITE_FILE;

  public static void ensureTables() {
    // Crea carpeta si no existe
    File dir = new File("/home/david/Documentos/java/papeleria/src/main/resources/data");
    if (!dir.exists())
      dir.mkdirs();

    String createUsuarios = """
            create table if not exists usuarios_local (
              id       text primary key,
              username text unique not null,
              password text not null,   -- solo para práctica
              rol      text not null,
              activo   integer not null -- 1/0
            );
        """;

    String createSync = """
            create table if not exists sync_state (
              key   text primary key,
              value text
            );
        """;

    try (Connection conn = DriverManager.getConnection(SQLITE_URL);
        Statement st = conn.createStatement()) {
      st.execute(createUsuarios);
      st.execute(createSync);
      System.out.println("✅ SQLite listo en: " + SQLITE_FILE);
    } catch (SQLException e) {
      System.err.println("⚠️ Error creando tablas locales: " + e.getMessage());
    }
  }

  /** Conexión simple a SQLite */
  public static Connection connect() throws SQLException {
    return DriverManager.getConnection(SQLITE_URL);
  }
}
