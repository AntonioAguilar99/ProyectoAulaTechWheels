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

    private final String archivoUsers = new File("src/DATA/Usuario.json").getAbsolutePath();
    private final Gson gson = new Gson();//Libreria
    private List<Usuario> usuarios;

    private List<Usuario> cargarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (Reader reader = new FileReader(archivoUsers)) {
            Gson gson = new Gson();
            Usuario[] users = gson.fromJson(reader, Usuario[].class);
            if (users != null) {
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
        try (FileWriter writer = new FileWriter(archivoUsers)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los usuarios: " + e.getMessage());
        }
    }

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
        if (numeroDocumento == null || numeroDocumento.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Documento inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<Usuario> usuarios = cargarUsuarios();

        // Buscar si existe y validar rol
        boolean encontrado = false;
        for (Usuario u : usuarios) {
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

        if (eliminado) {
            guardarUsuarios(usuarios);
            JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }

        return eliminado;

    }

}
