

package techwheels.Clases;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "compra")
public class Compra implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del cliente
    @Column(name = "nombre_cliente")
    private String nombreCliente;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(name = "metodo_pago")
    private String metodoPago;

    // Información del producto comprado
    @Column(name = "nombre_producto")
    private String nombreProducto;

    @Column(name = "descripcion_producto")
    private String descripcionProducto;

    @Column(name = "precio_producto")
    private double precioProducto;

    @Column(name = "cantidad")
    private int cantidad;

    // Nuevos campos
    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;

    @Column(name = "subtotal")
    private double subtotal;

    @Column(name = "total")
    private double total;

    // Constructor vacío
    public Compra() {
    }

    // Constructor con parámetros (incluyendo los nuevos campos)
    public Compra(String nombreCliente, String tipoDocumento, String numeroDocumento,
                  String metodoPago, String nombreProducto, String descripcionProducto,
                  double precioProducto, int cantidad, LocalDateTime fechaCompra) {
        this.nombreCliente = nombreCliente;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.metodoPago = metodoPago;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioProducto = precioProducto;
        this.cantidad = cantidad;
        this.fechaCompra = fechaCompra;

        // Calcular subtotal y total al construir
        this.subtotal = precioProducto * cantidad;
        this.total = this.subtotal; // Aquí puedes agregar lógica para impuestos o descuentos
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }



    // Método privado para recalcular subtotal y total
    private void actualizarMontos() {
        this.subtotal = this.precioProducto * this.cantidad;
        this.total = this.subtotal; // Aquí añade impuestos o descuentos si quieres
    }
}
