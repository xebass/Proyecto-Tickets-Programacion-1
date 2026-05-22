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

                InputStream input = getClass().getResourceAsStream("/Conexion/db_properties.properties");

                prop.load(input);

                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.user");
                String password = prop.getProperty("db.password");

                Class.forName("org.postgresql.Driver");

                cn = DriverManager.getConnection(url, user, password);

                System.out.println("Conexion exitosa a la base de datos.");

            } catch (Exception e) {

                System.out.println("Error de conexion: " + e.getMessage());

            }

            return cn;
        }
}
