package techwheels.Clases;

// autor: Mariana Jimenez
/**
 * Clase auxiliar para manejar el carrito de compras en memoria.
 */
public class ItemCarrito {

    private GestionProductos producto;
    private int cantidad;

    public ItemCarrito(GestionProductos producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public GestionProductos getProducto() {
        return producto;
    }

    public void setProducto(GestionProductos producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return producto.getPrecio(); // Asumiendo que tu clase tiene un método getPrecio()
    }

    public double getSubtotal() {
        return getPrecioUnitario() * cantidad;
    }
}