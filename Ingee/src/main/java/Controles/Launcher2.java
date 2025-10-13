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
            // --- 1️⃣ Cargar vista de apertura de caja ---
            FXMLLoader cajaLoader = new FXMLLoader(getClass().getResource("/Controles/AperturaCajaView.fxml"));
            Parent cajaRoot = cajaLoader.load();

            Stage cajaStage = new Stage();
            cajaStage.setTitle("Apertura de Caja");
            cajaStage.setScene(new Scene(cajaRoot));
            cajaStage.setResizable(false);
            cajaStage.initModality(Modality.APPLICATION_MODAL);

            // Si se cierra la ventana de apertura, cierra todo el programa
            cajaStage.setOnCloseRequest(event -> System.exit(0));
            cajaStage.showAndWait();

            // --- 2️⃣ Cargar ventana principal ---
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Controles/Main.fxml"));
            Parent mainRoot = mainLoader.load();

            primaryStage.setTitle("Cerrajería Nueva Troya - Sistema de Gestión");
            primaryStage.setScene(new Scene(mainRoot));
            primaryStage.setResizable(true);

            // --- 3️⃣ Cerrar caja automáticamente al salir del sistema ---
            primaryStage.setOnCloseRequest(event -> {
                try {
                    CajaManager cajaManager = CajaManager.getInstancia();
                    cajaManager.cerrarCaja();
                    System.out.println("✅ Caja cerrada correctamente al salir del sistema.");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("⚠️ No se pudo cerrar la caja automáticamente.");
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
