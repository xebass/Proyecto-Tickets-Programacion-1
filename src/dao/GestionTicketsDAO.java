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
    
    public boolean generarTicket(ModelGestionTickets t) {
    String sql = "INSERT INTO ticket(partido_id, numero_asiento, seccion, precio, estado) VALUES (?, ?, ?, ?, ?) ";

    try {
        Connection con = connF.getConection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, t.getPartido_id());
        ps.setString(2, t.getNumero_asiento());
        ps.setString(3, t.getSeccion());
        ps.setDouble(4, t.getPrecio());
        ps.setString(5, t.getEstado());

        ps.executeUpdate();
        return true;

    } catch (SQLException ex) {
        System.out.println("Error al generar ticket: " + ex.getMessage());
        return false;
    }
}
}
