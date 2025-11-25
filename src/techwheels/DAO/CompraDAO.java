/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package techwheels.DAO;

import Controller.Sesion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
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
      try (Reader reader = new FileReader(archivoCompras)) {

        Type listType = new TypeToken<List<Compra>>(){}.getType();
        List<Compra> compras = gson.fromJson(reader, listType);

        return compras != null ? compras : new ArrayList<>();

    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
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
        
       /*public boolean cancelarCompra(String idCompra) {
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
    }*/
    
    //Cambia el estado de la compra de "ACTIVA" a "CANCELADA"
    public boolean cancelarCompra(String idCompra) {

        // Se carga el archivo JSON y se convierte en una LISTA de objetos Compra.
        // → La estructura de datos principal es: List<Compra>
        List<Compra> compras = cargarCompras();

        for (Compra c : compras) {
            if (c.getId().equals(idCompra)) {
                // En lugar de eliminar la compra, solo se modifica su estado.
                // Esto es posible porque List<Compra> es una estructura dinámica y editable.
                c.setEstado("CANCELADA");  // cambia estado en vez de eliminar
                break;
            }
        }

        // Se guarda la LISTA nuevamente en el archivo JSON.
        // Gson convierte la lista a un arreglo JSON.
        try (FileWriter writer = new FileWriter(archivoCompras)) {
            gson.toJson(compras, writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Compra> cargarCompras1() {
        try (Reader reader = new FileReader(archivoCompras)) {
            // Se especifica el tipo List<Compra> para que Gson convierta el JSON correctamente.
            // JSON (arreglo) → List<Compra>
            Type listType = new TypeToken<List<Compra>>() {
            }.getType();
            
             // Se deserializa el archivo JSON y se convierte en una LISTA.
             // Esta lista es la estructura de datos que usa el programa internamente.
            List<Compra> compras = gson.fromJson(reader, listType);

             // Si el archivo está vacío o tiene null, se devuelve una lista vacía.
            return compras != null ? compras : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //  Lista compras canceladas para admnistrador
    public List<Compra> listarCanceladas() {
        List<Compra> todas = listarCompras();
        List<Compra> canceladas = new ArrayList<>();

        for (Compra c : todas) {
            if ("CANCELADA".equalsIgnoreCase(c.getEstado())) {
                canceladas.add(c);
            }
        }
        return canceladas;
    }
    
    
    public List<Compra> listarCanceladasUsuario() {
        List<Compra> usuario = cargarComprasDeUsuario();
        List<Compra> canceladas = new ArrayList<>();

        for (Compra c : usuario) {
            if ("CANCELADA".equalsIgnoreCase(c.getEstado())) {
                canceladas.add(c);
            }
        }
        return canceladas;
    }

}
