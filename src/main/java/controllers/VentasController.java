package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class VentasController {

    @FXML
    private Button btnNuevaVenta;



    @FXML
    private void nuevaVenta(ActionEvent event) {
        System.out.println("Botón 'Nueva venta' presionado");
        // Aquí pones la lógica para crear una nueva venta
    }
}
