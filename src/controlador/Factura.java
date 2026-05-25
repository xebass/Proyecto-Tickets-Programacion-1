/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.ClienteModelo;
import modelo.ModelGestionTickets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Factura {

    public static String generarTextoRecibo(ClienteModelo cliente, List<ModelGestionTickets> tickets, 
                                            double subtotal, double descuento, double iva, double total, 
                                            String metodoPago, String facturaNit) {
        
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        // Encabezado del Estadio / Torneo
        
        sb.append("         Tickets SATS         \n");
        sb.append("=========================================\n");
        sb.append("Fecha/Hora: ").append(dtf.format(LocalDateTime.now())).append("\n");
        sb.append("Método de Pago: ").append(metodoPago.toUpperCase()).append("\n");
        
        // Tipo de Documento (Factura o Recibo)
        if (facturaNit == null || facturaNit.trim().isEmpty() || facturaNit.equalsIgnoreCase("C/F")) {
            sb.append("Documento:   RECIBO CONSUMIDOR FINAL\n");
            sb.append("NIT:         C/F\n");
        } else {
            sb.append("Documento:   FACTURA ELECTRÓNICA\n");
            sb.append("NIT:         ").append(facturaNit.trim()).append("\n");
        }
        
        sb.append("Cliente:     ").append(cliente.getNombre()).append(" ").append(cliente.getApellido()).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append("DESCRIPCIÓN DE ENTRADAS:\n");
        
        // Listar cada ticket comprado
       for (ModelGestionTickets t : tickets) {
            sb.append(" * Ticket Boleto ID: ").append(t.getId()).append("\n");
            sb.append("   Precio Unitario:          Q ").append(t.getPrecio()).append("\n");
        }
        
        // Totales y Desglose de Ley en Guatemala
        sb.append("-----------------------------------------\n");
        sb.append(String.format("Subtotal:                   Q %,.2f\n", subtotal));
        sb.append(String.format("Descuento aplicado:         Q %,.2f\n", descuento));
        
        // Si es Factura, es buena práctica y obligatorio mostrar el desglose del IVA
        if (!facturaNit.equalsIgnoreCase("C/F")) {
            sb.append(String.format("IVA Desglosado (12%%):       Q %,.2f\n", iva));
        }
        
        sb.append("=========================================\n");
        sb.append(String.format("TOTAL PAGADO:               Q %,.2f\n", total));
        sb.append("=========================================\n");
        sb.append("   Gracias por su compra    \n");
        sb.append("=========================================");
        
        return sb.toString();
    }
}
