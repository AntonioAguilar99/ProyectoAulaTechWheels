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
import techwheels.Clases.Compra;

/**
 *
 * @author ASUS
 */
public class CompraDAO {
     private final String archivoCompras = new File("src/DATA/Compras.json").getAbsolutePath();
     private final Gson gson = new Gson();//Libreria
     private List<Compra> compra;
     
       private List<Compra> cargarCompras() {
      List<Compra> lista = new ArrayList<>();
      try (Reader reader = new FileReader(archivoCompras)) {
        Gson gson = new Gson();
        Compra[] compras = gson.fromJson(reader, Compra[].class);
        if (compras != null) {
            lista = new ArrayList<>(Arrays.asList(compras)); 
        }
    } catch (IOException e) {
        System.out.println("⚠ No se encontró el archivo: " + archivoCompras);
        e.printStackTrace();
    }
    return lista;
}
       public List<Compra> listarCompras() {
        return cargarCompras();
    }
       
        public void guardarCompra(Compra compra) {
        List<Compra> compras = cargarCompras();
        compra.setId((long) (compras.size() + 1));
        compras.add(compra);

        try (FileWriter writer = new FileWriter(archivoCompras)) {
            gson.toJson(compras, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    
}
