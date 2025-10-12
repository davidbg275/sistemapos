package controllers;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.AuthRepository;
import models.AuthRepositoryImpl;
import models.DatabaseLocal;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class LoginControllerFX {

  @FXML
  private TextField txtUsuario;
  @FXML
  private PasswordField txtPassword;
  @FXML
  private Button btnIngresar;
  @FXML
  private Label lblEstado;

  private final AuthRepository repo = new AuthRepositoryImpl();
  private final LoginController usecase = new LoginController(repo);

  @FXML
  public void initialize() {
    // Asegura SQLite local disponible
    DatabaseLocal.ensureTables();

    // (Opcional) Intentar un pull al arrancar si hay red.
    try {
      repo.pullAllUsersFromSupabase();
      lblEstado.setText("Base local sincronizada.");
    } catch (Exception e) {
      lblEstado.setText("Sincronización omitida (sin red).");
    }
  }

  @FXML
  private void onIngresar() {
    String user = txtUsuario.getText().trim();
    String pass = txtPassword.getText();

    if (user.isEmpty() || pass.isEmpty()) {
      lblEstado.setText("Ingresa usuario y contraseña.");
      return;
    }

    btnIngresar.setDisable(true);
    lblEstado.setText("Validando...");

    // Como es demo, lo hacemos directo; para UI fluida, mover a Task<>/thread.
    var res = usecase.login(user, pass);

    if (res.ok) {
      lblEstado.setText("Acceso " + res.modo + " | Rol: " + res.rol);
      // TODO: aquí navegas a la siguiente pantalla según el rol
      // por ejemplo: abrir vista de ventas si VENDEDOR, etc.

      try {
        var url = getClass().getResource("/views/MainFX.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        MainController main = loader.getController();
        main.configureForRole(res.rol); // pásale el rol

        Stage stage = (Stage) btnIngresar.getScene().getWindow();
        stage.setScene(new Scene(root));

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds(); // excluye barra superior
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        stage.show();

      } catch (Exception e) {
        e.printStackTrace();
        lblEstado.setText("NO SE PUDO ABRIR LA PANTALLA: " + e.getMessage());
      }

    } else {
      if (res.modo == LoginController.LoginMode.ONLINE) {
        lblEstado.setText("Credenciales incorrectas (ONLINE).");
      } else if (res.modo == LoginController.LoginMode.OFFLINE) {
        lblEstado.setText("Credenciales incorrectas (OFFLINE).");
      } else {
        lblEstado.setText("Error de login.");
      }
    }

    btnIngresar.setDisable(false);
  }
}
