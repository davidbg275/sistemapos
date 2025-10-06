package models;

public class Usuario {
  private final String username;
  private final String rol;
  private final boolean activo;

  public Usuario(String username, String rol, boolean activo) {
    this.username = username;
    this.rol = rol;
    this.activo = activo;
  }

  public String getUsername() {
    return username;
  }

  public String getRol() {
    return rol;
  }

  public boolean isActivo() {
    return activo;
  }
}
