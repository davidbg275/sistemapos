package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class MainController {

  @FXML
  private Button btnVentas;
  @FXML
  private Button btnInventario;
  @FXML
  private Button btnReportes;
  @FXML
  private Button btnUsuarios;

  @FXML
  private Button btnConfig;

  @FXML
  private AnchorPane contentArea;


  /** Llamar DESPUÉS de cargar el FXML. */
  public void configureForRole(String rol) {
    if (rol == null)
      rol = "";

    if (rol.equalsIgnoreCase("VENDEDOR")) {
      // Vendedor: limitar permisos
      btnVentas.setDisable(false);
      btnUsuarios.setDisable(true);
      btnReportes.setDisable(false);
      btnInventario.setDisable(false);
      btnConfig.setDisable(true);
    } else if (rol.equalsIgnoreCase("ADMIN")) {
      // Admin: todo activo
      btnVentas.setDisable(false);
      btnUsuarios.setDisable(false);
      btnReportes.setDisable(false);
      btnInventario.setDisable(false);
      btnConfig.setDisable(false);
     
    }
  }

  // Handlers de ejemplo (ajusta a tus flujos reales)
  

  @FXML
  private void onVentas() throws IOException {
    Parent fxml = FXMLLoader.load(getClass().getResource("/views/ventas.fxml"));
    contentArea.getChildren().removeAll();
    contentArea.getChildren().setAll(fxml);
  }

  @FXML
  private void onInventario() {
    /* abrir módulo inventario */ }

  @FXML
  private void onReportes() {
    /* abrir módulo reportes */ }

  @FXML
  private void onUsuarios() {
    /* abrir módulo usuarios */ }

  @FXML
  private void onConfig() {
    /* abrir configuración admin */ }

  @FXML
  private void onSalir() {
    System.exit(0);
  }
}
