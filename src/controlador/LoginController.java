/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.LoginDAO;
import modelo.modelLogin;
import modelo.modelLog;
public class LoginController {
    private final LoginDAO dao = new LoginDAO();
    
    public void nuevoUsuario(String username, String password, String name, String lastname, String email, String telefono, String rol, boolean estado){
        modelLogin user = new modelLogin(0, username, password, name, lastname, email, telefono, rol, estado);
        dao.guardarUsuario(user);
    }
    
    public String validarInfo(String username, String password){
        modelLog user= new modelLog(username, password);
        return dao.ValidarUser(user);
    }
}
