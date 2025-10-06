package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    var url = MainFX.class.getResource("/views/login.fxml");
    if (url == null) {
      throw new IllegalStateException("❌ No se encontró /views/login.fxml en resources");
    }

    FXMLLoader loader = new FXMLLoader(url);
    Scene scene = new Scene(loader.load(), 360, 240);
    stage.setTitle("Login - Papelería POS");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
