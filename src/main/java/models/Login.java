package models;
import java.io.Console;
import java.sql.*;
import java.util.Scanner;

public class Login {
  // ⬇️ Rellena con tus datos de Supabase
  private static final String DB_URL = "jdbc:postgresql://db.uxeuvpgbghjuvmsxrukd.supabase.co:5432/postgres";
  private static final String DB_USER = "postgres";
  private static final String DB_PASS = "admin123";

  public static void main(String[] args) {
    // 1) Pedir credenciales por terminal
    Scanner sc = new Scanner(System.in);
    System.out.print("Usuario: ");
    String username = sc.nextLine().trim();

    String password;
    Console cons = System.console();
    if (cons != null) {
      char[] pw = cons.readPassword("Contraseña: ");
      password = new String(pw);
    } else {
      System.out.print("Contraseña: ");
      password = sc.nextLine();
    }

    // 2) Conectar y validar en la nube (Supabase)
    String sql = "select rol, activo from public.usuarios " +
        "where username = ? and password = ? limit 1";

    try (Connection cx = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        PreparedStatement ps = cx.prepareStatement(sql)) {

      ps.setString(1, username);
      ps.setString(2, password);

      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) {
          System.out.println("❌ Usuario o contraseña incorrectos.");
          return;
        }
        boolean activo = rs.getBoolean("activo");
        String rol = rs.getString("rol");

        if (!activo) {
          System.out.println("⛔ Usuario inactivo. Contacta al admin.");
          return;
        }

        System.out.println("✅ Acceso concedido. Rol: " + rol);
        // Aquí ya podrías decidir menú según rol
      }
    } catch (SQLException e) {
      System.out.println("⚠️ Error de conexión/consulta: " + e.getMessage());
    }
  }
}
