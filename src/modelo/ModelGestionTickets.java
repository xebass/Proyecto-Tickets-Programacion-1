/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author tequi
 */
public class ModelGestionTickets {
    int id;
    int partido_id;
    String numero_asiento;
    String seccion;
    double precio;
    String estado;
    String tipo_pago;

    
    public ModelGestionTickets() {
}
    
    
    public ModelGestionTickets(int id, int partido_id, String numero_asiento, String seccion,double precio, String estado, String tipo_pago) {
        this.id = id;
        this.partido_id = partido_id;
        this.numero_asiento = numero_asiento;
        this.seccion = seccion;
        this.precio = precio;
        this.estado = estado;
        this.tipo_pago = tipo_pago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartido_id() {
        return partido_id;
    }

    public void setPartido_id(int partido_id) {
        this.partido_id = partido_id;
    }

    public String getNumero_asiento() {
        return numero_asiento;
    }

    public void setNumero_asiento(String numero_asiento) {
        this.numero_asiento = numero_asiento;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    
    
}