package Controles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher2 extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Carga el nuevo FXML principal con pestañas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controles/Main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setTitle("Cerrajería Nueva Troya - Sistema de Gestión");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}
