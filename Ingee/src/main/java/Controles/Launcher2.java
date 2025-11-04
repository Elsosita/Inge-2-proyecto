package Controles;

import Clases.CajaManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Launcher2 extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // PASO 1: Cargar la vista de LOGIN
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/Controles/LoginView.fxml"));
            Parent loginRoot = loginLoader.load();

            // 1. CAPTURAR LA INSTANCIA DEL CONTROLLER
            LoginController loginController = loginLoader.getController();

            Stage loginStage = new Stage();
            loginStage.setTitle("Acceso al Sistema");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setResizable(false);

            loginStage.showAndWait(); // Espera el cierre

            // 2. COMPROBACIÓN CORREGIDA
            // Llama al método en el objeto 'loginController', no en la clase.
            if (!loginController.isLoginExitoso()) {
                // Si el login no fue exitoso (el usuario cerró con la X o falló al ingresar)
                System.exit(0); // Termina la aplicación inmediatamente
                return; // Detiene la ejecución del resto del método start
            }
            //Cargar vista de apertura de caja
            FXMLLoader cajaLoader = new FXMLLoader(getClass().getResource("/Controles/AperturaCajaView.fxml"));
            Parent cajaRoot = cajaLoader.load();

            Stage cajaStage = new Stage();
            cajaStage.setTitle("Apertura de Caja");
            cajaStage.setScene(new Scene(cajaRoot));
            cajaStage.setResizable(false);
            cajaStage.initModality(Modality.APPLICATION_MODAL);

            // Si se cierra la ventana de apertura, cierra el prog
            cajaStage.setOnCloseRequest(event -> System.exit(0));
            cajaStage.showAndWait();

            //Cargar ventana principal
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Controles/Main.fxml"));
            Parent mainRoot = mainLoader.load();

            primaryStage.setTitle("Cerrajería Nueva Troya - Sistema de Gestión");
            primaryStage.setScene(new Scene(mainRoot));
            primaryStage.setResizable(true);

            //Cerrar caja automáticamente al salir del sistema
            primaryStage.setOnCloseRequest(event -> {
                try {
                    CajaManager cajaManager = CajaManager.getInstancia();
                    cajaManager.cerrarCaja();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
