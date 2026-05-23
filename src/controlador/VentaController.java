package VentaController;

import VentaDAO.VentaDAO;
import VentaModel.Cliente;
import VentaModel.Partido;
import VentaModel.Ticket;

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

    // ── datos ────────────────────────────────────────────────────────────
    public List<Partido> obtenerPartidos() {
        return dao.obtenerPartidos();
    }

    public List<Ticket> obtenerTickets(Partido partido) {
        double mult = getMultiplicador(partido.getFase());
        List<Ticket> tickets = dao.obtenerTicketsDisponibles(partido.getId());
        // ajustar precio según la fase
        for (Ticket t : tickets) {
            t.setPrecio(t.getPrecio() * mult);
        }
        return tickets;
    }

    public List<Cliente> obtenerClientes() {
        return dao.obtenerClientes();
    }

    public boolean guardarCliente(Cliente cl) {
        return dao.guardarCliente(cl);
    }

    // ── cálculos ─────────────────────────────────────────────────────────
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

    public double calcularIVA(List<Ticket> seleccionados) {
        double base = calcularSubtotal(seleccionados) - calcularDescuento(seleccionados);
        return base * 0.12;
    }

    public double calcularTotal(List<Ticket> seleccionados) {
        return calcularSubtotal(seleccionados)
             - calcularDescuento(seleccionados)
             + calcularIVA(seleccionados);
    }

    // ── confirmar venta ──────────────────────────────────────────────────
    public boolean confirmarVenta(Cliente cliente, List<Ticket> tickets, int usuarioId) {
        double total = calcularTotal(tickets);
        return dao.guardarVenta(cliente, tickets, usuarioId, total);
    }
}