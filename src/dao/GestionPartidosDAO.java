/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.CreateConection;
import java.sql.*;
import javax.swing.JOptionPane;
import modelo.ModelGestionPartidos;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
public class GestionPartidosDAO {
    private final CreateConection connF = new CreateConection();
    
    
    public boolean guardarPartido(ModelGestionPartidos partido){
        String sql = "INSERT INTO partido (equipo_local, equipo_visitante, fecha, estadio, ciudad, capacidad, estado, fase) VALUES(?,?,?,?,?,?,?,?)";
        try {
            Connection con = connF.getConection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, partido.getLocal());
            ps.setString(2, partido.getVisitante());
            ps.setDate(3, java.sql.Date.valueOf(partido.getFecha()));
            ps.setString(4, partido.getEstadio());
            ps.setString(5, partido.getCuidad());
            ps.setInt(6, partido.getCapacidad());
            ps.setString(7, partido.getEstado());
            ps.setString(8, partido.getFase());
            ps.executeUpdate();
            ps.close();
            con.close();
            
            return true;
        } catch (SQLException ex) {
            System.getLogger(GestionPartidosDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }
    
    public List<ModelGestionPartidos> obtenerDatos(){
        List<ModelGestionPartidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM public.partido";
        try {
            
            
            Connection con = connF.getConection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                ModelGestionPartidos partido = new ModelGestionPartidos(rs.getInt("id"), 
                        rs.getString("equipo_local"), rs.getString("equipo_visitante"), rs.getDate("fecha").toLocalDate(), 
                        rs.getString("estadio"), 
                        rs.getString("ciudad"), rs.getInt("capacidad"), rs.getString("estado"), rs.getString("fase"));
                lista.add(partido);
            }
            
            
        } catch (SQLException ex) {
            System.getLogger(GestionPartidosDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }
    
    public boolean actualizar (ModelGestionPartidos partido){
        String sql = "UPDATE partido set equipo_local = ?, equipo_visitante =?, fecha = ?, estadio = ?, ciudad = ?, capacidad = ?, estado = ?, fase = ? WHERE id = ?";
        try {
            
            Connection con = connF.getConection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, partido.getLocal());
            ps.setString(2, partido.getVisitante());
            ps.setDate(3, java.sql.Date.valueOf(partido.getFecha()));
            ps.setString(4, partido.getEstadio());
            ps.setString(5, partido.getCuidad());
            ps.setInt(6, partido.getCapacidad());
            ps.setString(7, partido.getEstado());
            ps.setString(8, partido.getFase());
            ps.setInt(9, partido.getId());
            ps.executeUpdate();
            ps.close();
            con.close();
            
            return true;
        } catch (SQLException ex) {
            System.getLogger(GestionPartidosDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }
}
