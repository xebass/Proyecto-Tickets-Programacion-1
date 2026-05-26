package controlador;

import dao.VentaDAO;
import modelo.ClienteModelo;
import modelo.ModelGestionPartidos;
import modelo.ModelGestionTickets; 

import java.util.List;

public class VentaController {

    private final VentaDAO dao = new VentaDAO();

    
    public double getMultiplicador(String fase) {
        if (fase == null) return 1.0;
        switch (fase.toUpperCase()) {
            case "DIECISEISAVOS": return 1.20;
            case "OCTAVOS":       return 1.40;
            case "CUARTOS":       return 1.60;
            case "SEMIFINAL":     return 2.00;
            case "FINAL":         return 2.50;
            default:              return 1.00; 
        }
    }

    
    public List<ModelGestionPartidos> obtenerPartidos() {
        return dao.obtenerPartidos();
    }

   public List<ModelGestionTickets> obtenerTickets(ModelGestionPartidos partido) {
        
        return dao.obtenerTicketsDisponibles(partido.getId());
    }

    public List<ClienteModelo> obtenerClientes() {
        return dao.obtenerClientes();
    }

    public boolean guardarCliente(ClienteModelo cl) {
        return dao.guardarCliente(cl);
    }

    
    public double calcularSubtotal(List<ModelGestionTickets> seleccionados) {
        double sub = 0;
        for (ModelGestionTickets t : seleccionados) sub += t.getPrecio();
        return sub;
    }

    public double calcularDescuento(List<ModelGestionTickets> seleccionados) {
        int n = seleccionados.size();
        double sub = calcularSubtotal(seleccionados);
        if (n >= 10) return sub * 0.07;
        if (n > 5)   return sub * 0.05;
        return 0;
    }

   
    public double calcularIVA(List<ModelGestionTickets> seleccionados) {
    double base = calcularSubtotal(seleccionados) - calcularDescuento(seleccionados);
    return base * 0.12;
}


    
    public double calcularTotal(List<ModelGestionTickets> seleccionados) {
    double base = calcularSubtotal(seleccionados) - calcularDescuento(seleccionados);
    return base + calcularIVA(seleccionados);
}

    
    public boolean confirmarVenta(ClienteModelo cliente, List<ModelGestionTickets> tickets, int usuarioId, String metodoPago, String facturaNit) {
        double total = calcularTotal(tickets);
        
        return dao.guardarVenta(cliente, tickets, usuarioId, total, metodoPago, facturaNit);
    }
}