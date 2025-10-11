package Controles;

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
                // Cargar la ventana de Apertura de Caja
                FXMLLoader cajaLoader = new FXMLLoader(getClass().getResource("/Controles/AperturaCajaView.fxml"));
                Parent cajaRoot = cajaLoader.load();

                Stage cajaStage = new Stage();
                cajaStage.setTitle("Apertura de Caja");
                cajaStage.setScene(new Scene(cajaRoot));
                cajaStage.setResizable(false);
                cajaStage.initModality(Modality.APPLICATION_MODAL);

                // ⚠️ Si se cierra la ventana con la X, termina el programa
                cajaStage.setOnCloseRequest(event -> {
                    System.exit(0);
                });

                cajaStage.showAndWait();

                // Si el flujo llega aquí, significa que se aceptó correctamente
                // (por ejemplo, desde el botón "Aceptar")
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Controles/Main.fxml"));
                Parent mainRoot = mainLoader.load();

                primaryStage.setTitle("Cerrajería Nueva Troya - Sistema de Gestión");
                primaryStage.setScene(new Scene(mainRoot));
                primaryStage.setResizable(true);
                primaryStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    public static void main(String[] args) {
        launch(args);
    }
}
