/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.CreateConection;
import java.sql.*;
import javax.swing.JOptionPane;
import modelo.modelLogin;
import modelo.modelLog;
import java.util.List;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;
public class LoginDAO {
    private final CreateConection connFac = new CreateConection();
    
    
    
    public boolean guardarUsuario(modelLogin user){
        String Spassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String sql = "INSERT INTO public.usuario(username, password, name, lastname, email, telefono, rol, estado) VALUES(?,?,?,?,?,?,?,?)";
        try {
            Connection con = connFac.getConection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, Spassword);
            ps.setString(3, user.getName());
            ps.setString(4, user.getLastname());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getTelefono());
            ps.setString(7, user.getRol());
            ps.setBoolean(8, user.isEstado());
            
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException ex) {
            System.getLogger(LoginDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }
    
    public String ValidarUser(modelLog user){
        String sql = "SELECT * FROM public.usuario WHERE public.usuario.username = (?)";
        try {
            
            Connection con = connFac.getConection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());

            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                String PasswordDB = rs.getString("password");
                if (BCrypt.checkpw(user.getPassword(), PasswordDB)){//esta onda compara lo que se escribio con la db (osea todo el if)
                    String rol = rs.getString("rol");
                    ps.close();
                    con.close();
                    return rol;
                }
            
            }else{
                JOptionPane.showMessageDialog(null, "El Usuario/Contraseña es incorrecto.");
           
            }
            ps.close();
            con.close();
            
            
        } catch (SQLException ex) {
            System.getLogger(LoginDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }
    
    
}
