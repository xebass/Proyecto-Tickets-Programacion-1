package modelo;

public class Ticket {

    private int id;
    private int partidoId;
    private String numeroAsiento;
    private String seccion; // VIP, PREFERENCIAL, GENERAL
    private double precio;
    private String estado;  // DISPONIBLE, VENDIDO, RESERVADO

    public Ticket() {
        id = 0;
        partidoId = 0;
        numeroAsiento = "";
        seccion = "";
        precio = 0.0;
        estado = "DISPONIBLE";
    }

    public Ticket(int id, int partidoId, String numeroAsiento,
                  String seccion, double precio, String estado) {
        this.id = id;
        this.partidoId = partidoId;
        this.numeroAsiento = numeroAsiento;
        this.seccion = seccion;
        this.precio = precio;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPartidoId() { return partidoId; }
    public void setPartidoId(int partidoId) { this.partidoId = partidoId; }

    public String getNumeroAsiento() { return numeroAsiento; }
    public void setNumeroAsiento(String numeroAsiento) { this.numeroAsiento = numeroAsiento; }

    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Asiento " + numeroAsiento + " [" + seccion + "] - Q " + String.format("%,.2f", precio);
    }
}
