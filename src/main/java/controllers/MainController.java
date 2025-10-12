package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class MainController {

  @FXML private Button btnVentas;
  @FXML private Button btnInventario;
  @FXML private Button btnReportes;
  @FXML private Button btnUsuarios;

  @FXML private Menu     menuAdmin;
  @FXML private MenuItem itemConfig;

  @FXML private HBox panelAvanzado;

  /** Llamar DESPUÉS de cargar el FXML. */
  public void configureForRole(String rol) {
    if (rol == null) rol = "";

    if (rol.equalsIgnoreCase("VENDEDOR")) {
      // Vendedor: limitar permisos
      btnUsuarios.setDisable(true);
      // Ejemplos: desactiva lo que no deba usar
      // btnReportes.setDisable(true);
      // btnInventario.setDisable(true);

      menuAdmin.setDisable(true);
      itemConfig.setDisable(true);
      panelAvanzado.setVisible(false);
      panelAvanzado.setManaged(false); // que no ocupe espacio
    }
    else if (rol.equalsIgnoreCase("ADMIN")) {
      // Admin: todo activo
      btnUsuarios.setDisable(false);
      btnReportes.setDisable(false);
      btnInventario.setDisable(false);
      menuAdmin.setDisable(false);
      itemConfig.setDisable(false);
      panelAvanzado.setVisible(true);
      panelAvanzado.setManaged(true);
    }
    else {
      // Rol desconocido: modo mínimo
      btnUsuarios.setDisable(true);
      menuAdmin.setDisable(true);
      itemConfig.setDisable(true);
      panelAvanzado.setVisible(false);
      panelAvanzado.setManaged(false);
    }
  }

  // Handlers de ejemplo (ajusta a tus flujos reales)
  @FXML private void onVentas()      { /* abrir módulo ventas */ }
  @FXML private void onInventario()  { /* abrir módulo inventario */ }
  @FXML private void onReportes()    { /* abrir módulo reportes */ }
  @FXML private void onUsuarios()    { /* abrir módulo usuarios */ }
  @FXML private void onConfig()      { /* abrir configuración admin */ }
  @FXML private void onSalir()       { System.exit(0); }
}
