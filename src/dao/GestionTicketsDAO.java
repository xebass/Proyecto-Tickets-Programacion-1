/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import conexion.CreateConection;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import modelo.ModelGestionTickets;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import modelo.ModelGestionPartidos;

public class GestionTicketsDAO {
    private final CreateConection connF = new CreateConection();
    
    public List<ModelGestionPartidos> obtenerPartidos(){
        List<ModelGestionPartidos> listap = new ArrayList<>();
            
        String sql = "SELECT * FROM public.partido WHERE estado = 'DISPONIBLE'";
        try {
            Connection con = connF.getConection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                 ModelGestionPartidos partido = new ModelGestionPartidos(rs.getInt("id"), 
                        rs.getString("equipo_local"), rs.getString("equipo_visitante"), rs.getDate("fecha").toLocalDate(), 
                        rs.getString("estadio"), 
                        rs.getString("ciudad"), rs.getInt("capacidad"), rs.getString("estado"));
                listap.add(partido);
            }
            
            
        } catch (SQLException ex) {
            System.getLogger(GestionTicketsDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return listap;
    } 
}
