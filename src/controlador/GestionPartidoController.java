/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.GestionPartidosDAO;
import java.time.LocalDate;
import java.util.List;
import modelo.ModelGestionPartidos;
public class GestionPartidoController {
    private final GestionPartidosDAO dao = new GestionPartidosDAO();
    
    public void NuevoPartido(String local, String visitante, LocalDate fecha, String estadio, String cuidad, int capacidad, String estado){
        ModelGestionPartidos partido = new ModelGestionPartidos(0, local, visitante, fecha, estadio, cuidad, capacidad, estado);
        dao.guardarPartido(partido);
    }
    
    public List<ModelGestionPartidos> ObtenerPartido(){
        return dao.obtenerDatos();
    }
    
    public void actualizarPartido(int id, String local, String visitante, LocalDate fecha, String estadio, String cuidad, int capacidad, String estado){
        ModelGestionPartidos partido = new ModelGestionPartidos(id, local, visitante, fecha, estadio, cuidad, capacidad, estado);
        dao.actualizar(partido);
    }
    
}
