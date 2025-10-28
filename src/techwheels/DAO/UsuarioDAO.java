/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package techwheels.DAO;

import com.google.gson.Gson;
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
      private final String archivoUsers = "C:\\Users\\ASUS\\Documents\\MARIA PAULINA\\TechWheels\\src\\DATA\\Usuario.json";
     private final Gson gson = new Gson();//Libreria
     private List<Usuario> usuarios;
      
     
        private List<Usuario> cargarUsuarios() {
       List<Usuario> lista = new ArrayList<>();
          try {
        Gson gson = new Gson();
        Reader reader = new FileReader(archivoUsers); //Lee los archivos json
        Usuario[] users= gson.fromJson(reader, Usuario[].class);
        lista = Arrays.asList(users);
        reader.close();
              } catch (IOException e) {
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
