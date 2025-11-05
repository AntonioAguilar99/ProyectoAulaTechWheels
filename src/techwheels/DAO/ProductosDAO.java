/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package techwheels.DAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import techwheels.Clases.GestionProductos;

/**
 *
 * @author ASUS
 */
public class ProductosDAO {
     private final String archivoProductos = new File("src/DATA/Productos.json").getAbsolutePath();
    private final Gson gson = new Gson();

    private List<GestionProductos> cargarProductos() {
       List<GestionProductos> lista = new ArrayList<>();
          try {
        Gson gson = new Gson();
        Reader reader = new FileReader(archivoProductos); //Lee los archivos json
        GestionProductos[] users= gson.fromJson(reader, GestionProductos[].class);
        lista = Arrays.asList(users);
        reader.close();
              } catch (IOException e) {
                  e.printStackTrace();
               }
                  return lista;
              }
        
        
         public List<GestionProductos> listarProductos() {
        return cargarProductos();
    }
         
         public void guardarProductos(List<GestionProductos> productos) {
        try (FileWriter writer = new FileWriter(archivoProductos)) {
            gson.toJson(productos, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los Productoss: " + e.getMessage());
        }
    }
}
