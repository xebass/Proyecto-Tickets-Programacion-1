package dao;

import conexion.CreateConection;
import modelo.ClienteModelo;
import modelo.ModelGestionPartidos;
import modelo.ModelGestionTickets; 

import java.sql.*;
import java.time.LocalDate; 
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    private final CreateConection connFactory = new CreateConection();

    // =========================================================================
    // 1. OBTENER PARTIDOS DISPONIBLES (Ajustado al constructor del Modelo)
    // =========================================================================
    public List<ModelGestionPartidos> obtenerPartidos() {
        List<ModelGestionPartidos> lista = new ArrayList<>();
        
        // Agregamos ciudad y capacidad a la consulta para cumplir con el constructor del modelo
        String sql = "SELECT id, equipo_local, equipo_visitante, fecha, estadio, ciudad, capacidad, estado " +
                     "FROM partido WHERE UPPER(estado) = 'DISPONIBLE' ORDER BY fecha";
                     
        try (Connection conn = connFactory.getConection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                // Conversión segura de java.sql.Timestamp a java.time.LocalDate
                Timestamp timestamp = rs.getTimestamp("fecha");
                LocalDate fechaLocalDate = (timestamp != null) ? timestamp.toLocalDateTime().toLocalDate() : null;

                // Llamada exacta al constructor existente de tus compañeros:
                // (int id, String local, String visitante, LocalDate fecha, String estadio, String ciudad, int capacidad, String estado)
                ModelGestionPartidos p = new ModelGestionPartidos(
                        rs.getInt("id"),
                        rs.getString("equipo_local"),
                        rs.getString("equipo_visitante"),
                        fechaLocalDate,
                        rs.getString("estadio"),
                        rs.getString("ciudad"),
                        rs.getInt("capacidad"),
                        rs.getString("estado"),
                        rs.getString("fase")
                );
                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error obtenerPartidos: " + ex.getMessage());
        }
        return lista;
    }

    // =========================================================================
    // 2. OBTENER TICKETS DISPONIBLES POR PARTIDO
    // =========================================================================
    public List<ModelGestionTickets> obtenerTicketsDisponibles(int partidoId) {
        List<ModelGestionTickets> lista = new ArrayList<>();
        String fasePartido = "GRUPOS"; // Por defecto si no encuentra nada

        // 1. Averiguamos la fase del partido directamente de la BD
        String sqlFase = "SELECT fase FROM partido WHERE id = ?";
        // 2. Traemos los tickets (Agregamos tipo_pago a la consulta por si viene de la BD)
        String sqlTickets = "SELECT id, partido_id, numero_asiento, seccion, precio, estado, tipo_pago " +
                            "FROM ticket WHERE partido_id = ? AND UPPER(estado) = 'DISPONIBLE' " +
                            "ORDER BY seccion, numero_asiento";
                 
        try (Connection conn = connFactory.getConection()) {
            
            // Primero obtenemos la fase
            try (PreparedStatement psFase = conn.prepareStatement(sqlFase)) {
                psFase.setInt(1, partidoId);
                try (ResultSet rsFase = psFase.executeQuery()) {
                    if (rsFase.next()) {
                        fasePartido = rsFase.getString("fase");
                    }
                }
            }

            // Calculamos el multiplicador según la fase obtenida de la BD
            double mult = 1.0;
            if (fasePartido != null) {
                switch (fasePartido.toUpperCase()) {
                    case "DIECISEISAVOS": mult = 1.20; break;
                    case "OCTAVOS":       mult = 1.40; break;
                    case "CUARTOS":       mult = 1.60; break;
                    case "SEMIFINAL":     mult = 2.00; break;
                    case "FINAL":         mult = 2.50; break;
                    default:              mult = 1.00; break;
                }
            }

            // Ahora cargamos los tickets y les aplicamos el multiplicador directamente
            try (PreparedStatement psTck = conn.prepareStatement(sqlTickets)) {
                psTck.setInt(1, partidoId);
                try (ResultSet rs = psTck.executeQuery()) { 
                    while (rs.next()) {
                        double precioBase = rs.getDouble("precio");
                        
                        // CORRECCIÓN: Pasamos los 7 parámetros exactos que pide el nuevo constructor
                        ModelGestionTickets t = new ModelGestionTickets(
                                rs.getInt("id"),
                                rs.getInt("partido_id"),
                                rs.getString("numero_asiento"),
                                rs.getString("seccion"),
                                precioBase * mult, // El precio ya sale calculado desde el DAO
                                rs.getString("estado"),
                                rs.getString("tipo_pago") // ◄ Faltaba este parámetro
                        );
                        lista.add(t);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error obtenerTicketsDisponibles: " + ex.getMessage());
        }
        return lista;
    }

    // =========================================================================
    // 3. OBTENER CLIENTES
    // =========================================================================
    public List<ClienteModelo> obtenerClientes() {
        List<ClienteModelo> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, telefono, email, direccion FROM cliente ORDER BY apellido, nombre";
                     
        try (Connection conn = connFactory.getConection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                ClienteModelo cl = new ClienteModelo(
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

    // =========================================================================
    // 4. GUARDAR CLIENTE NUEVO 
    // =========================================================================
    public boolean guardarCliente(ClienteModelo cl) {
        String sql = "INSERT INTO cliente (nombre, apellido, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";
                     
        try (Connection conn = connFactory.getConection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getApellido());
            ps.setString(3, cl.getTelefono());
            ps.setString(4, cl.getEmail());
            ps.setString(5, cl.getDireccion());
            ps.executeUpdate();
            
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    cl.setIdCliente(keys.getInt(1)); 
                }
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("Error guardarCliente: " + ex.getMessage());
        }
        return false;
    }

    // =========================================================================
    // 5. GUARDAR VENTA Y DETALLES (PROCESO TRANSACCIONAL)
    // =========================================================================
    public boolean guardarVenta(ClienteModelo cliente, List<ModelGestionTickets> tickets, int usuarioId, double total) {
        if (tickets == null || tickets.isEmpty()) return false;
        
        Connection conn = null;
        try {
            conn = connFactory.getConection();
            conn.setAutoCommit(false); 

            int ventaId = -1;
            String sqlVenta = "INSERT INTO venta (cliente_id, usuario_id, total) VALUES (?, ?, ?)";
            
            try (PreparedStatement ps = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, cliente.getIdCliente()); 
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
                throw new SQLException("No se pudo obtener el ID de la venta cabecera.");
            }

            String sqlDetalle = "INSERT INTO detalle_venta (venta_id, ticket_id, precio, iva) VALUES (?, ?, ?, ?)";
            String sqlTicket  = "UPDATE ticket SET estado = 'VENDIDO' WHERE id = ?";
            
            try (PreparedStatement psDet = conn.prepareStatement(sqlDetalle);
                 PreparedStatement psTck = conn.prepareStatement(sqlTicket)) {
                
                for (ModelGestionTickets t : tickets) {
                    double precioTotalTicket = t.getPrecio();
                    double precioBase = precioTotalTicket / 1.12;
                    double ivaTicket = precioTotalTicket - precioBase;

                    psDet.setInt(1, ventaId);
                    psDet.setInt(2, t.getId());
                    psDet.setDouble(3, precioTotalTicket);
                    psDet.setDouble(4, ivaTicket);
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