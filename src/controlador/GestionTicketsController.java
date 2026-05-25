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
}
