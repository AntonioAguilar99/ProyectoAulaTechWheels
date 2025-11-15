

package techwheels.Clases;

import java.util.List;




public class Compra{

    
    private Long id;
    private String nombreCliente;
    private String apellidoCliente;
    private String tipoDocumento;
    private String numeroDocumento;
    private String metodoPago;
    private List<CarritoTemp> productos;
    private String direccion;
    private String fecha;
    private double subtotal;
    private double total;
    
    public Compra() {
    }

    // Constructor con parámetros (incluyendo los nuevos campos)
    public Compra(String nombreCliente,String apellidoCliente,  String tipoDocumento, String numeroDocumento,
                  String metodoPago, List<CarritoTemp> productos, String direccion,  String fecha, double subtotal, double total) {
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.metodoPago = metodoPago;
        this.productos = productos;
        this.fecha = fecha;
        // Calcular subtotal y total al construir
        this.subtotal = calcularSubtotal();
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
    
     public String getApellidoCliente() {
        return apellidoCliente;
    }
     
    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
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
    
     public List<CarritoTemp> getProductos(){
         return productos; 
     }
     
    public void setProductos(List<CarritoTemp> productos) {
        this.productos = productos; 
    }
    
     public String getDireccion() {
        return direccion  ;
    }
     
    public void setDireccion(String direccion) {
        this.direccion = direccion; 
    } 

    public String getFechaCompra() {
        return fecha;
    }

    public void setFechaCompra(String fecha) {
        this.fecha = fecha;
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
   
     private double calcularSubtotal() {
        double subtotal = 0;
        if (productos != null) {
            for (CarritoTemp p : productos) {
                subtotal += p.getPrecioProducto() * p.getCantidad();
            }
        }
        return subtotal;
    }
}
