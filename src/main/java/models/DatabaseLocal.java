package models;

import java.sql.*;

public class DatabaseLocal {

  // Ruta donde se guardará tu base local
  private static final String SQLITE_URL = "jdbc:sqlite:/home/david/Documentos/java/papeleria/src/main/resources/data/papeleria.db";

  // Método que crea las tablas si no existen
  public static void ensureLocalSqlite() {
    // 🔹 Tu código SQL va aquí dentro (es exactamente lo que copiaste)
    String createUsuarios = """
            create table if not exists usuarios_local (
              id text primary key,
              username text unique not null,
              password text not null,
              rol text not null,
              activo integer not null
            );
        """;

    String createSync = """
            create table if not exists sync_state (
              key text primary key,
              value text
            );
        """;

    // 🔹 Conexión y ejecución
    try (Connection conn = DriverManager.getConnection(SQLITE_URL);
        Statement st = conn.createStatement()) {

      st.execute(createUsuarios);
      st.execute(createSync);
      System.out.println("✅ Tablas locales listas en SQLite (" + SQLITE_URL + ")");

    } catch (SQLException e) {
      System.err.println("⚠️ Error al crear tablas locales: " + e.getMessage());
    }
  }
}
