package Managers; // O Clases, si prefieres

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashGenerator {

    public static void main(String[] args) {
        // Establece la contraseña inicial que deseas usar para el login (ej: 'admin123')
        String passwordPlana = "PONE TU CONTRASEÑA ACA";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(passwordPlana.getBytes());
            String hash = Base64.getEncoder().encodeToString(hashBytes);

            System.out.println("---------------------------------------------");
            System.out.println("Contraseña Plana: " + passwordPlana);
            System.out.println("HASH GENERADO: " + hash);
            System.out.println("---------------------------------------------");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}