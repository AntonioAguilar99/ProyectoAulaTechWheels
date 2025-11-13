/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package techwheels.DAO;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import techwheels.Clases.CarritoTemp;


/**
 *
 * @author ASUS
 */
public class CarritoDAO {
      private final String archivoCarrito = new File("src/DATA/Carrito.json").getAbsolutePath();
     private final Gson gson = new Gson();//Libreria
     private List<CarritoTemp> carrito;
     
     
     private List<CarritoTemp> cargarCarrito() {
        List<CarritoTemp> lista = new ArrayList<>();
        try {
            File file = new File(archivoCarrito);
            
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                guardarCarrito(new ArrayList<>());
            }

            try (Reader reader = new FileReader(file)) {
                CarritoTemp[] carritos= gson.fromJson(reader, CarritoTemp[].class);
                if (carritos != null) {
                    lista = new ArrayList<>(Arrays.asList(carritos));
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los productos en el carrito: " + e.getMessage());
        }
        return lista;
    }
     
     public List<CarritoTemp> listarcarrito() {
        return cargarCarrito();
    }
     
      public void guardarCarrito(List<CarritoTemp> carritos) {
        try (FileWriter writer = new FileWriter(archivoCarrito)) {
            gson.toJson(carritos, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los productos en el carrito: " + e.getMessage());
        }
    }
      
    public void agregarProductoCarrito(CarritoTemp nuevo) {
        List<CarritoTemp> carrito = cargarCarrito();

        // Si ya existe el producto, solo aumenta la cantidad
        for (CarritoTemp p : carrito) {
            if (p.getNombreProducto().equalsIgnoreCase(nuevo.getNombreProducto())) {
                p.setCantidad(p.getCantidad() + nuevo.getCantidad());
                guardarCarrito(carrito);
                return;
            }
        }

        carrito.add(nuevo);
        guardarCarrito(carrito);
    }
    
      public  void eliminarProductoCarrito(String nombreProducto) {
        List<CarritoTemp> carrito = cargarCarrito();
        carrito.removeIf(p -> p.getNombreProducto().equalsIgnoreCase(nombreProducto));
        guardarCarrito(carrito);
    }
      
   
    public void actualizarCantidad(String nombreProducto, int nuevaCantidad) {
        List<CarritoTemp> carrito = cargarCarrito();
        for (CarritoTemp p : carrito) {
            if (p.getNombreProducto().equalsIgnoreCase(nombreProducto)) {
                p.setCantidad(nuevaCantidad);
                break;
            }
        }
        guardarCarrito(carrito);
    }
    
     public void vaciarCarrito() {
        guardarCarrito(new ArrayList<>());
    }
}
