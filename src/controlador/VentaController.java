package controlador;

import dao.VentaDAO;
import modelo.ClienteModelo;
import modelo.ModelGestionPartidos;
import modelo.Ticket; // Importación explícita agregada

import java.util.List;

public class VentaController {

    private final VentaDAO dao = new VentaDAO();

    // ── multiplicador de precio según fase ───────────────────────────────
    public double getMultiplicador(String fase) {
        if (fase == null) return 1.0;
        switch (fase.toUpperCase()) {
            case "DIECISEISAVOS": return 1.20;
            case "OCTAVOS":       return 1.40;
            case "CUARTOS":       return 1.60;
            case "SEMIFINAL":     return 2.00;
            case "FINAL":         return 2.50;
            default:              return 1.00; // GRUPOS
        }
    }

    // ── datos (Adaptados a los modelos unificados) ───────────────────────
    public List<ModelGestionPartidos> obtenerPartidos() {
        return dao.obtenerPartidos();
    }

   public List<Ticket> obtenerTickets(ModelGestionPartidos partido) {
        // El DAO ya se encarga de verificar la fase en la BD y aplicar el multiplicador
        return dao.obtenerTicketsDisponibles(partido.getId());
    }

    public List<ClienteModelo> obtenerClientes() {
        return dao.obtenerClientes();
    }

    public boolean guardarCliente(ClienteModelo cl) {
        return dao.guardarCliente(cl);
    }

    // ── cálculos (Estructura de precios e IVA de Guatemala) ──────────────
    public double calcularSubtotal(List<Ticket> seleccionados) {
        double sub = 0;
        for (Ticket t : seleccionados) sub += t.getPrecio();
        return sub;
    }

    public double calcularDescuento(List<Ticket> seleccionados) {
        int n = seleccionados.size();
        double sub = calcularSubtotal(seleccionados);
        if (n >= 10) return sub * 0.07;
        if (n > 5)   return sub * 0.05;
        return 0;
    }

    // El precio de la interfaz ya incluye el IVA, calculamos el desglose para la factura/vista
    public double calcularIVA(List<Ticket> seleccionados) {
        double totalCobrado = calcularSubtotal(seleccionados) - calcularDescuento(seleccionados);
        double baseFacturable = totalCobrado / 1.12;
        return totalCobrado - baseFacturable;
    }

    // El total final neto que el cliente va a pagar en caja
    public double calcularTotal(List<Ticket> seleccionados) {
        return calcularSubtotal(seleccionados) - calcularDescuento(seleccionados);
    }

    // ── confirmar venta ──────────────────────────────────────────────────
    public boolean confirmarVenta(ClienteModelo cliente, List<Ticket> tickets, int usuarioId) {
        double total = calcularTotal(tickets);
        return dao.guardarVenta(cliente, tickets, usuarioId, total);
    }
}