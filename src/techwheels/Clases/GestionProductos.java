package techwheels.Clases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestionProductos {
    private String id;
    private String fecha;
    private String nombre;
    private String entregadoPor;
    private String recibidoPor;
    private String descripcion;
    private String marca;
    private int cantidad;
    private String categoria;
    private double precio;
    private boolean disponible;

    public GestionProductos(String id, String nombre, String entregadoPor, String recibidoPor,
                            String descripcion, String marca, int cantidad, String categoria, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.entregadoPor = entregadoPor;
        this.recibidoPor = recibidoPor;
        this.descripcion = descripcion;
        this.marca = marca;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.precio = precio;
        this.disponible = cantidad > 0;

        // Fecha actual en formato legible
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters y setters
    public String getId() { return id; }
    public String getFecha() { return fecha; }
    public String getNombre() { return nombre; }
    public String getEntregadoPor() { return entregadoPor; }
    public String getRecibidoPor() { return recibidoPor; }
    public String getDescripcion() { return descripcion; }
    public String getMarca() { return marca; }
    public int getCantidad() { return cantidad; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.disponible = cantidad > 0;
    }

    public void setId(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
