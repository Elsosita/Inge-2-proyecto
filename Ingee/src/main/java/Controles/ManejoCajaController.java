package Controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ManejoCajaController {
    @FXML private Button btnPago;

    @FXML
    private void abrirPago() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/IngresoPagoView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ingreso de Pago");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            ((Stage)btnPago.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRetiro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/IngresoRetiroView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ingreso de Retiro");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            ((Stage)btnPago.getScene().getWindow()).close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
