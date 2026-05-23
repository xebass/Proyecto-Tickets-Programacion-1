package VentaDAO;


import VentaModel.Cliente;
import VentaModel.Partido;
import VentaModel.Ticket;
import conexion.CreateConection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    private final CreateConection connFactory = new CreateConection();

    //obtener partidos
    public List<Partido> obtenerPartidos() {
        List<Partido> lista = new ArrayList<>();
        
        String sql = "SELECT id, equipo_local, equipo_visitante, fecha, estadio, fase, estado " +
                     "FROM partido WHERE estado = 'DISPONIBLE' ORDER BY fecha";
        try (Connection conn = connFactory.getConection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Partido p = new Partido(
                        rs.getInt("id"),
                        rs.getString("equipo_local"),
                        rs.getString("equipo_visitante"),
                        rs.getTimestamp("fecha"),
                        rs.getString("estadio"),
                        rs.getString("fase"),
                        rs.getString("estado")
                );
                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error obtenerPartidos: " + ex.getMessage());
        }
        return lista;
    }

    //obtenertickets
    public List<Ticket> obtenerTicketsDisponibles(int partidoId) {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT id, partido_id, numero_asiento, seccion, precio, estado " +
                     "FROM ticket WHERE partido_id = ? AND estado = 'DISPONIBLE' " +
                     "ORDER BY seccion, numero_asiento";
        try (Connection conn = connFactory.getConection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, partidoId);
            try (ResultSet rs = ps.executeQuery()) { // Uso de try-with-resources para cerrar el RS automáticamente
                while (rs.next()) {
                    Ticket t = new Ticket(
                            rs.getInt("id"),
                            rs.getInt("partido_id"),
                            rs.getString("numero_asiento"),
                            rs.getString("seccion"),
                            rs.getDouble("precio"),
                            rs.getString("estado")
                    );
                    lista.add(t);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error obtenerTickets: " + ex.getMessage());
        }
        return lista;
    }

    // obtenerclientes
    public List<Cliente> obtenerClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, telefono, email, direccion " +
                     "FROM cliente ORDER BY apellido, nombre";
        try (Connection conn = connFactory.getConection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente cl = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion")
                );
                lista.add(cl);
            }
        } catch (SQLException ex) {
            System.out.println("Error obtenerClientes: " + ex.getMessage());
        }
        return lista;
    }

    public boolean guardarCliente(Cliente cl) {
        String sql = "INSERT INTO cliente (nombre, apellido, telefono, email, direccion) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connFactory.getConection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getApellido());
            ps.setString(3, cl.getTelefono());
            ps.setString(4, cl.getEmail());
            ps.setString(5, cl.getDireccion());
            ps.executeUpdate();
            
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) cl.setId(keys.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("Error guardarCliente: " + ex.getMessage());
        }
        return false;
    }

    //guardar
    public boolean guardarVenta(Cliente cliente, List<Ticket> tickets, int usuarioId, double total) {
        if (tickets == null || tickets.isEmpty()) return false;
        
        Connection conn = null;
        try {
            conn = connFactory.getConection();
            conn.setAutoCommit(false);

            int ventaId = -1;
            
            String sqlVenta = "INSERT INTO venta (fecha, cliente_id, usuario_id, total) VALUES (NOW(), ?, ?, ?)";
            
            try (PreparedStatement ps = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, cliente.getId());
                ps.setInt(2, usuarioId);
                ps.setDouble(3, total);
                ps.executeUpdate();
                
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        ventaId = rs.getInt(1);
                    }
                }
            }

            if (ventaId == -1) {
                throw new SQLException("No se pudo obtener el ID generado para la venta.");
            }

            
            String sqlDetalle = "INSERT INTO detalle_venta (venta_id, ticket_id, precio, iva) VALUES (?, ?, ?, ?)";
            String sqlTicket  = "UPDATE ticket SET estado = 'VENDIDO' WHERE id = ?";
            
            
            double ivaPorTicket = (total / tickets.size()) * 0.12; 

            try (PreparedStatement psDet = conn.prepareStatement(sqlDetalle);
                 PreparedStatement psTck = conn.prepareStatement(sqlTicket)) {
                
                for (Ticket t : tickets) {
                    
                    psDet.setInt(1, ventaId);
                    psDet.setInt(2, t.getId());
                    psDet.setDouble(3, t.getPrecio());
                    psDet.setDouble(4, ivaPorTicket);
                    psDet.addBatch();

                    
                    psTck.setInt(1, t.getId());
                    psTck.addBatch();
                }
                
                
                psDet.executeBatch();
                psTck.executeBatch();
            }

            conn.commit(); 
            return true;

        } catch (SQLException ex) {
            System.out.println("Error crítico en guardarVenta: " + ex.getMessage());
            if (conn != null) {
                try {
                    System.out.println("Ejecutando Rollback de la transacción...");
                    conn.rollback(); 
                } catch (SQLException x) {
                    System.out.println("Error en Rollback: " + x.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
        return false;
    }
}