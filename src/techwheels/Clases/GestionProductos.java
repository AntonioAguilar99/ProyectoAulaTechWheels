package techwheels.Clases;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Antonio Aguilar
 */


public class GestionProductos implements Serializable {

    private Long codigo;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer cantidad;
    private String categoria;
    private String marca;
    private Date fecha;

    public GestionProductos() {
        this.fecha = new Date(); // fecha por defecto
    }

    public GestionProductos(String nombre, String descripcion, Double precio, Integer cantidad, String categoria, String marca, Date fecha) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.marca = marca;
        this.fecha = fecha;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}



