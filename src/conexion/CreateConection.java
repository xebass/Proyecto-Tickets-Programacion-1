package conexion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CreateConection {

     public Connection getConection() {
    Connection cn = null;
    try {
        Properties prop = new Properties();
        InputStream input = null;
        
        // Primero intenta cargarlo desde dentro del JAR
        input = getClass().getResourceAsStream("/conexion/db_properties.properties");
        
        // Si no lo encuentra dentro del JAR, lo busca junto al ejecutable
        if (input == null) {
            String ruta = System.getProperty("user.dir") + "/db_properties.properties";
            java.io.File archivo = new java.io.File(ruta);
            if (archivo.exists()) {
                input = new java.io.FileInputStream(archivo);
            }
        }
        
        if (input == null) {
            System.out.println("No se encontró el archivo properties");
            return null;
        }
        
        prop.load(input);
        
        String url = prop.getProperty("db.url");
        String user = prop.getProperty("db.user");
        String password = prop.getProperty("db.password");
        
        Class.forName("org.postgresql.Driver");
        cn = DriverManager.getConnection(url, user, password);
        System.out.println("Conexion exitosa.");
        
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
    return cn;
}
}
