

package Controller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import techwheels.Clases.CarritoTemp;
import techwheels.Clases.GestionProductos;
import techwheels.DAO.ProductosDAO;

/**
 *
 * @author Antonio Aguilar
 */
public class InventarioController {
    
    // Objeto DAO que permite acceder a los datos de los productos desde los archivos
     private ProductosDAO productoDAO;
    
   public InventarioController() {
       
        // Se inicializa el objeto DAO para poder usar sus métodos más adelante
        this.productoDAO = new ProductosDAO();
    }
   
   public void cargarProductos(DefaultTableModel p){
       
         // Se obtiene la lista de productos usando el DAO
         List<GestionProductos> producto = productoDAO.listarProductos();
         System.out.println("Productos cargados: " + producto.size());//s un método de las listas en Java que devuelve la cantidad de elementos que hay en la lista.
         
        for (GestionProductos prd : producto) {//recorre todos los elementos de la lista reservas Se toma un objeto reserva de la lista
            Object[] fila = {//Se crea un arreglo de objetos. Con los datos de la reserva
              
                prd.getNombre(),
                prd.getDescripcion(),
                prd.getMarca(),
                prd.getCategoria(),
                prd.getPrecio(),
                prd.isDisponible(),
               
                    
            };
             // Se agrega la fila al modelo de la tabla
             
            p.addRow(fila);//inserta esa fila en la tabla visible para que el usuario pueda verla en la interfaz gráfica.
        }
    }
   
    public void refrescarTabla(DefaultTableModel modelo) {
        
        // Limpia todas las filas del modelo de la tabla
        modelo.setRowCount(0);
        
         // Vuelve a cargar los usuarios desde el DAO y agrega las filas al modelo
        cargarProductos(modelo);
        JOptionPane.showMessageDialog(null, "Tabla actualizada correctamente.");
    }
   
   
}
