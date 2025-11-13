

package Controller;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import techwheels.Clases.GestionProductos;
import techwheels.DAO.ProductosDAO;

/**
 *
 * @author Antonio Aguilar
 */
public class InventarioController {
     private ProductosDAO productoDAO;
    
   public InventarioController() {
        this.productoDAO = new ProductosDAO();
    }
   
   public void cargarProductos(DefaultTableModel p){
         List<GestionProductos> producto = productoDAO.listarProductos();
         System.out.println("Productos cargados: " + producto.size());
        for (GestionProductos prd : producto) {//recorre todos los elementos de la lista reservas.Se toma un objeto reserva de la lista
            Object[] fila = {//Se crea un arreglo de objetos. Con los datos de la reserva
                prd.getNombre(),
                prd.getDescripcion(),
                prd.getMarca(),
                prd.getCategoria(),
                prd.getPrecio(),
                prd.isDisponible()
                    
            };
            p.addRow(fila);
        }
    }
}
