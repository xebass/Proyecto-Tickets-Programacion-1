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
        
        // Busca el properties en la misma carpeta que el JAR
        String ruta = System.getProperty("user.dir") + "/db_properties.properties";
        java.io.FileInputStream input = new java.io.FileInputStream(ruta);
        
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
