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
import techwheels.Clases.Usuario;

/**
 *
 * @author ASUS
 */
public class UsuarioDAO {
      private final String archivoUsers = new File("src/DATA/Usuario.json").getAbsolutePath();
     private final Gson gson = new Gson();//Libreria
     private List<Usuario> usuarios;
      
     
       private List<Usuario> cargarUsuarios() {
    List<Usuario> lista = new ArrayList<>();
    try (Reader reader = new FileReader(archivoUsers)) {
        Gson gson = new Gson();
        Usuario[] users = gson.fromJson(reader, Usuario[].class);
        if (users != null) {
            lista = new ArrayList<>(Arrays.asList(users)); // ✅ lista editable
        }
    } catch (IOException e) {
        System.out.println("⚠ No se encontró el archivo: " + archivoUsers);
        e.printStackTrace();
    }
    return lista;
}
              
         public List<Usuario> listarUsuarios() {
        return cargarUsuarios();
    }
        
       public void guardarUsuarios(List<Usuario> usuarios) {
        try (FileWriter writer = new FileWriter(archivoUsers)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los usuarios: " + e.getMessage());
        }
    }
}
