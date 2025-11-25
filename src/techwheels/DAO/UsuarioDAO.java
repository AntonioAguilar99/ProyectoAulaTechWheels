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
import techwheels.Clases.Enumeraciones.RolUsuarioEnum;
import techwheels.Clases.Usuario;

/**
 *
 * @author ASUS
 */
public class UsuarioDAO {
    
    // Ruta ABSOLUTA del archivo JSON donde se almacenan los usuarios.
    // new File(...).getAbsolutePath() convierte la ruta relativa → absoluta.
    private final String archivoUsers = new File("src/DATA/Usuario.json").getAbsolutePath();
    private final Gson gson = new Gson();//Libreria
    private List<Usuario> usuarios;

    
     // -------------------------------------------------------------
    // MÉTODO PRIVADO: cargarUsuarios()
    // Encargado de leer el archivo JSON y convertirlo en una lista.
    // -------------------------------------------------------------
    private List<Usuario> cargarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (Reader reader = new FileReader(archivoUsers)) {
            
            // Se crea un nuevo Gson para leer el archivo.
            Gson gson = new Gson();
             // Se lee el JSON y se convierte en un ARREGLO de Usuario.
            // El JSON debe tener formato: [ {}, {}, {} ]
            Usuario[] users = gson.fromJson(reader, Usuario[].class);
            if (users != null) {
            // Si el archivo contenía usuarios, los convertimos en una lista dinámica (ArrayList)
                lista = new ArrayList<>(Arrays.asList(users));
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
        
        // Convertimos la LISTA de usuarios (List<Usuario>)
        // a formato JSON utilizando Gson.
        // Gson transforma los objetos Java → JSON y los escribe en el archivo.
        try (FileWriter writer = new FileWriter(archivoUsers)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            // En caso de que falle la escritura del archivo,
            // se muestra un mensaje de error en pantalla.
            JOptionPane.showMessageDialog(null, "Error al guardar los usuarios: " + e.getMessage());
        }
    }
    // Se cargan todos los usuarios desde el archivo JSON.
    // Esto devuelve una LISTA de objetos Usuario.

    public Usuario buscarPorDocumento(String numeroDocumento) {
        List<Usuario> usuarios = cargarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getNumeroDocumento().equals(numeroDocumento)) {
                return u;
            }
        }
        return null; // No encontrado
    }
    
    //METODO ELIMINAR USUARIO POR NUMERO DE DOCUMENTO
    public boolean eliminarUsuario(String numeroDocumento) {
        
        // Verifica que el número de documento no venga vacío o nulo.

        if (numeroDocumento == null || numeroDocumento.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Documento inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

         // Carga todos los usuarios desde el archivo JSON.
        List<Usuario> usuarios = cargarUsuarios();

        // Buscar si existe y validar rol
        boolean encontrado = false;
        for (Usuario u : usuarios) {
             // Compara el número de documento (quitando espacios)
            if (u.getNumeroDocumento().trim().equals(numeroDocumento.trim())) {
                encontrado = true;
                if (u.getRol().equals(RolUsuarioEnum.ADMINISTRADOR)) {
                    JOptionPane.showMessageDialog(null,
                            "No se puede eliminar al administrador principal.",
                            "Acceso denegado",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Eliminar usando removeIf
        boolean eliminado = usuarios.removeIf(u -> u.getNumeroDocumento().trim().equals(numeroDocumento.trim()));

        
    // Si removeIf eliminó al usuario correctamente
        if (eliminado) {
            guardarUsuarios(usuarios); // Guarda la nueva lista actualizada en el JSON.
            JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
 
        // Retorna si se eliminó o no.
        return eliminado;

    }

}
