package controlador;

import dao.GestionTicketsDAO;
import java.time.LocalDate;
import java.util.List;
import modelo.ModelGestionPartidos;
import modelo.ModelGestionTickets;
public class GestionTicketsController {
    private final GestionTicketsDAO dao = new GestionTicketsDAO();
    
    public List<ModelGestionPartidos> ObtenerPartido(){
        return dao.obtenerPartidos();
    }
    
    public boolean generarTicket(ModelGestionTickets t){
        return dao.generarTicket(t);
    }
    
    public String generarNumeroAsiento(int partido_id, String seccion){
        return dao.generarNumeroAsiento(partido_id, seccion);
    }
    
    public boolean descontarAsiento(int partido_id){
        return dao.descontarAsientos(partido_id);
    }    
    
    public int obtenerDisponibilidadSeccion(int partido_id, String seccion, int capacidad){
        return dao.obtenerDisponibilidadSeccion(partido_id, seccion, capacidad);
    }
    }
