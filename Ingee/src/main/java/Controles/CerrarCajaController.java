package Controles;

import Clases.Caja;
import Clases.CajaManager;
import Clases.SueldoLiquidacion;
// import ClasesDao.CajaDao; // Ya no es necesario si delegamos a Manager
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CerrarCajaController {

    @FXML
    private TextField txtTotal;

    @FXML
    private TextField txtEfectivo;

    @FXML
    private TextField txtDigital;

    @FXML
    private TextField txtRetirosSueldos;


    // Solo necesitamos la referencia al Manager y a la caja real
    private final CajaManager cajaManager = CajaManager.getInstancia();
    private Caja cajaAbiertaOriginal; // La caja real antes del cálculo

    @FXML
    public void initialize() {
        try {
            // Ya no es necesario obtener el DAO ni inicializarlo
            // cajaDao = CajaDao.getInstancia();
            cajaAbiertaOriginal = CajaManager.getCajaAbierta(); // Obtener la caja en memoria

            if (cajaAbiertaOriginal != null) {
                // 1. Simular el cierre para obtener los montos finales
                Caja cajaSimulada = cajaManager.simularCierreCajaConSueldos();

                // 2. Calcular el monto total de retiros aplicados (Sueldo Fijo + Comisión)
                // Se calcula la diferencia entre el monto total original y el simulado
                float retirosAplicados = cajaAbiertaOriginal.getMontototal() - cajaSimulada.getMontototal();

                // 3. Mostrar los montos NETOS (simulados)
                txtTotal.setText(String.format("%.2f", cajaSimulada.getMontototal()));
                txtEfectivo.setText(String.format("%.2f", cajaSimulada.getMontoefectivo()));
                txtDigital.setText(String.format("%.2f", cajaSimulada.getMontodigital()));

                // 4. Mostrar el total de sueldos a descontar
                txtRetirosSueldos.setText(String.format("%.2f", retirosAplicados));

            } else {
                mostrarAlerta("Error", "No hay una caja abierta actualmente.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de BD", "No se pudo cargar la simulación de cierre.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onVerDetalleSueldos() {
        try {
            if (CajaManager.getCajaAbierta() == null) {
                mostrarAlerta("Error", "Debe haber una caja abierta para ver el detalle.", Alert.AlertType.ERROR);
                return;
            }

            // 1. Cargar el FXML de la nueva ventana de detalle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetallesSueldoView.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador de la nueva ventana
            DetalleSueldosController controller = loader.getController();

            // 3. Pasar los datos de liquidación al nuevo controlador
            // (Llamamos al método que creamos antes en el Manager)
            controller.setDetalleLiquidacion(cajaManager.obtenerDetalleLiquidacionSueldos());

            // 4. Mostrar la ventana como modal
            Stage stage = new Stage();
            stage.setTitle("Detalle de Liquidación de Sueldos");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (SQLException e) {
            mostrarAlerta("Error de BD", "Fallo al calcular los sueldos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la ventana: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onAceptar() {
        try {
            if (cajaAbiertaOriginal == null) {
                mostrarAlerta("Error", "No hay una caja abierta.", Alert.AlertType.ERROR);
                return;
            }

            // 🔥 SOLO LLAMAMOS AL MÉTODO NUEVO DEL MANAGER 🔥
            // El método 'cerrarCajaConSueldos' ya incluye:
            // 1. Calcular sueldos.
            // 2. Registrar los Retiros.
            // 3. Actualizar montos en la BD.
            // 4. Cambiar el estado a CERRADA en la BD.
            cajaManager.cerrarCajaConSueldos();

            mostrarAlerta("Éxito", "La caja se ha cerrado y los sueldos se han liquidado correctamente.", Alert.AlertType.INFORMATION);

            Stage stage = (Stage) txtTotal.getScene().getWindow();
            stage.close();

             javafx.application.Platform.exit();
             Platform.exit();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cerrar la caja: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}