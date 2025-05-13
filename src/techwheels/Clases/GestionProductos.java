package techwheels.Clases;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Antonio Aguilar
 */

@Entity(name = "productos") // nombre de la tabla en la BD
public class GestionProductos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long codigo; // Cambiado de String a Long

    @Column(length = 40, nullable = false)
    private String nombre;

    @Column(length = 255, nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(length = 30)
    private String categoria;

    @Column(length = 70)
    private String marca;

    @Temporal(TemporalType.DATE)
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



