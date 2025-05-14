

package techwheels.Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Antonio Aguilar
 */
@Entity(name = "compras")
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private Double subtotal;
    private Double impuestos;
    private Double descuento;
    private Double total;

    @Column(length = 30)
    private String metodoPago; // Ej: "EFECTIVO", "TARJETA"

    @Column(length = 30)
    private String estado; // Ej: "PAGADO", "PENDIENTE"

    @Column(length = 255)
    private String direccionEntrega;

    @Column(length = 255)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detalles = new ArrayList<>();

    // Constructor vacío requerido por JPA
    public Compra() {}

    public Compra(Long id, Date fecha, Double subtotal, Double impuestos, Double descuento, Double total, String metodoPago, String estado, String direccionEntrega, String observaciones, Usuario usuario, List<DetalleCompra> detalles) {
        this.id = id;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.descuento = descuento;
        this.total = total;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.direccionEntrega = direccionEntrega;
        this.observaciones = observaciones;
        this.usuario = usuario;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
    }

    // ✅ Método útil para agregar detalle desde código
    public void addDetalle(DetalleCompra detalle) {
        detalle.setCompra(this); // importante para mantener relación bidireccional
        this.detalles.add(detalle);
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getImpuestos() { return impuestos; }
    public void setImpuestos(Double impuestos) { this.impuestos = impuestos; }

    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }
}
