package Controles;

import Managers.AuthManager;
import Clases.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    // Obtener la instancia del Manager de autenticación
    private final AuthManager authManager = AuthManager.getInstancia();
    private boolean loginExitoso = false;

    // NUEVO GETTER
    public boolean isLoginExitoso() {
        return loginExitoso;
    }

    @FXML
    private void onLogin() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Debe ingresar usuario y contraseña.");
            return;
        }

        try {
            Usuario usuarioAutenticado = authManager.autenticar(usuario, password);

            if (usuarioAutenticado != null) {
                // ÉXITO: Cierra la ventana de login para continuar con el flujo
                this.loginExitoso = true;
                lblMensaje.setText("Acceso exitoso.");
                ((Stage) txtUsuario.getScene().getWindow()).close();
            } else {
                // FALLO: Muestra el error
                lblMensaje.setText("Usuario o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lblMensaje.setText("Error de conexión con la base de datos.");
        }
    }

    // Se mantiene la función principal de cierre de la aplicación en Launcher2
}