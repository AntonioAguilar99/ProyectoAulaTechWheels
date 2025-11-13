package techwheels.DAO;

import com.google.gson.Gson;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import techwheels.Clases.GestionProductos;

public class ProductosDAO {

    private final String archivoProductos = new File("src/DATA/Productos.json").getAbsolutePath();
    private final Gson gson = new Gson();

    
    private List<GestionProductos> cargarProductos() {
        List<GestionProductos> lista = new ArrayList<>();
        try {
            File file = new File(archivoProductos);
            
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                guardarProductos(new ArrayList<>());
            }

            try (Reader reader = new FileReader(file)) {
                GestionProductos[] productos = gson.fromJson(reader, GestionProductos[].class);
                if (productos != null) {
                    lista = new ArrayList<>(Arrays.asList(productos));
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los productos: " + e.getMessage());
        }
        return lista;
    }

    // ✅ Listar todos los productos
    public List<GestionProductos> listarProductos() {
        return cargarProductos();
    }

    // ✅ Guardar lista completa de productos en el JSON
    public void guardarProductos(List<GestionProductos> productos) {
        try (FileWriter writer = new FileWriter(archivoProductos)) {
            gson.toJson(productos, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los productos: " + e.getMessage());
        }
    }

    // ✅ Agregar un nuevo producto con ID único
    public void agregarProducto(GestionProductos nuevo) {
        if (nuevo.getId() == null || nuevo.getId().isEmpty()) {
            nuevo.setId(UUID.randomUUID().toString());
        }
        List<GestionProductos> productos = listarProductos();
        productos.add(nuevo);
        guardarProductos(productos);
    }

    // ✅ Eliminar producto por ID
    public void eliminarProducto(String id) {
        List<GestionProductos> productos = listarProductos();
        productos.removeIf(p -> p.getId() != null && p.getId().equals(id));
        guardarProductos(productos);
    }

    // ✅ Actualizar datos de un producto existente
    public void actualizarProducto(GestionProductos actualizado) {
        List<GestionProductos> productos = listarProductos();
        for (int i = 0; i < productos.size(); i++) {
            String prodId = productos.get(i).getId();
            if (prodId != null && prodId.equals(actualizado.getId())) {
                productos.set(i, actualizado);
                break;
            }
        }
        guardarProductos(productos);
    }
}
