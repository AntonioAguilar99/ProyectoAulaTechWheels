/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package techwheels.DAO;

import Controller.Sesion;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
        compra.setId(UUID.randomUUID().toString());
        compras.add(compra);

        try (FileWriter writer = new FileWriter(archivoCompras)) {
            gson.toJson(compras, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    public List<Compra> cargarComprasDeUsuario() {
        List<Compra> todas = listarCompras();
        List<Compra> filtradas = new ArrayList<>();

        if (Sesion.usuarioActual == null) {
            System.out.println("⚠ No hay usuario en sesión.");
            return filtradas;
        }

        // Filtrar compras por número de documento del usuario
        for (Compra c : todas) {
            if (c.getNumeroDocumento().equalsIgnoreCase(Sesion.usuarioActual.getNumeroDocumento())) {
                filtradas.add(c);
            }
        }

        return filtradas;
    }
    
    public Compra buscarCompraPorId(String id) {
        List<Compra> compras = listarCompras();

        for (Compra c : compras) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null; // No encontrada
    }
        
       public boolean cancelarCompra(String idCompra) {
        List<Compra> compras = cargarCompras();

        boolean eliminada = compras.removeIf(c -> c.getId().equals(idCompra));

        if (eliminada) {
            try (FileWriter writer = new FileWriter(archivoCompras)) {
                gson.toJson(compras, writer);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return eliminada;
    }
    
}
