package controllers;

import models.AuthRepository;
import models.Usuario;

/**
 * Orquesta el caso de uso de login:
 * - Intenta ONLINE (Supabase) con timeout corto.
 * - Si hay error de red, cae a OFFLINE (SQLite).
 * - Si ONLINE responde pero no coincide, devuelve fallo directo.
 */
public class LoginController {
  private final AuthRepository repo;

  public LoginController(AuthRepository repo) {
    this.repo = repo;
  }

  public ResultadoLogin login(String username, String password) {
    // 1) Intento ONLINE
    try {
      Usuario u = repo.loginOnline(username, password);
      if (u != null && u.isActivo()) {
        return new ResultadoLogin(true, u.getRol(), LoginMode.ONLINE);
      }
      // Si online respondió pero no hay coincidencia, ya no tiene caso ir offline
      return new ResultadoLogin(false, null, LoginMode.ONLINE);
    } catch (Exception net) {
      // 2) Sin red: OFFLINE
      try {
        Usuario u = repo.loginOffline(username, password);
        if (u != null && u.isActivo()) {
          return new ResultadoLogin(true, u.getRol(), LoginMode.OFFLINE);
        }
        return new ResultadoLogin(false, null, LoginMode.OFFLINE);
      } catch (Exception e2) {
        // Error inesperado en offline
        return new ResultadoLogin(false, null, LoginMode.ERROR);
      }
    }
  }

  // --- DTOs pequeñitos para devolver resultado legible a la vista ---

  public static class ResultadoLogin {
    public final boolean ok;
    public final String rol; // ADMIN / VENDEDOR / REPORT
    public final LoginMode modo; // ONLINE / OFFLINE / ERROR

    public ResultadoLogin(boolean ok, String rol, LoginMode modo) {
      this.ok = ok;
      this.rol = rol;
      this.modo = modo;
    }
  }

  public enum LoginMode {
    ONLINE, OFFLINE, ERROR
  }
}
