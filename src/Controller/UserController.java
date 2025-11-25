/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import techwheels.Clases.Enumeraciones.RolUsuarioEnum;
import techwheels.Clases.Usuario;
import techwheels.DAO.UsuarioDAO;

/**
 *
 * @author ASUS
 */
public class UserController {

    // Objeto DAO para acceder a los datos de usuarios (leer, guardar, eliminar)
    private UsuarioDAO userDAO;

    public UserController() {
        
        // Inicializa el DAO para poder usar sus métodos más adelante
        this.userDAO = new UsuarioDAO();
    }

    // Método que devuelve la lista completa de usuarios desde la base de datos
    public List<Usuario> listarUsuarios() {
        return userDAO.listarUsuarios();
    }

    // Recibe correo y contraseña, y devuelve el Usuario si las credenciales son correctas
    public Usuario login(String correo, String contraseña) {
        
        //// Obtiene todos los usuarios registrados
        List<Usuario> user = userDAO.listarUsuarios();
        
        
        // Recorre la lista de usuarios
        //"para cada objeto Usuario llamado usuarios que esté en la lista user, haz lo siguiente…user es la lista de todos los usuarios registrados (List<Usuario>).
        for (Usuario usuarios : user) {
            
            // Compara el correo y la contraseña
            if (usuarios.getCorreo().equalsIgnoreCase(correo)
                    && usuarios.getContraseña().equals(contraseña)) {

                // Si coincide, guarda el usuario en la sesión actual
                Sesion.usuarioActual = usuarios;
                return usuarios; // login exitoso
            }
        }
        return null; // no encontrado
    }

    public boolean registrarUsuario(Usuario nuevoUsuario) {
        
         // Obtiene la lista de todos los usuarios registrados actualmente
        List<Usuario> usuarios = userDAO.listarUsuarios();
 
        // Recorre la lista para verificar duplicados

        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(nuevoUsuario.getCorreo())) {
                JOptionPane.showMessageDialog(null, "El correo ya está registrado.");
                return false;
            }
            if (u.getNumeroDocumento().equalsIgnoreCase(nuevoUsuario.getNumeroDocumento())) {
                JOptionPane.showMessageDialog(null, "El número de documento ya está registrado.");
                return false;
            }

        }
 
        // Si no hay duplicados, agrega el nuevo usuario a la lista
        usuarios.add(nuevoUsuario);
        
        // Guarda la lista actualizada en el archivo mediante el DAO
        userDAO.guardarUsuarios(usuarios);
        JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");
        return true;
    }

    public void cargarUsuarios(DefaultTableModel u) {
        List<Usuario> usuario = userDAO.listarUsuarios();
        System.out.println("Usuarios cargados: " + usuario.size());
        
        for (Usuario user : usuario) {//recorre todos los elementos de la lista reservas.Se toma un objeto reserva de la lista
            Object[] fila = {//Se crea un arreglo de objetos. Con los datos de la reserva

                // Crea un arreglo de objetos que representa una fila de la tabla
                // Cada elemento del arreglo corresponde a una columna de la JTable
                user.getCodigo(),
                user.getNombres(),
                user.getApellidos(),
                user.getTipoDocumento(),
                user.getNumeroDocumento(),
                user.getCorreo(),
                user.getTelefono(),
                user.getContraseña(),
                user.getRol()
            };
            u.addRow(fila);
        }
    }

    public void refrescarTabla(DefaultTableModel modelo) {
        
        // Limpia todas las filas del modelo de la tabla
        modelo.setRowCount(0);
        
         // Vuelve a cargar los usuarios desde el DAO y agrega las filas al modelo
        cargarUsuarios(modelo);
        JOptionPane.showMessageDialog(null, "Tabla actualizada correctamente.");
    }
    
    
    // Método para eliminar un usuario dado su número de documento
    public boolean eliminarUsuario(String documento) {
        
        // Verifica que el documento no sea nulo ni vacío
        if (documento == null || documento.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Documento inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

         // Crea un objeto DAO para acceder a los datos de usuarios
        UsuarioDAO dao = new UsuarioDAO();
        
        // Obtiene la lista completa de usuarios
        List<Usuario> lista = dao.listarUsuarios();

        // Evitar eliminar ADMINISTRADOR
        for (Usuario u : lista) {
            if (u.getNumeroDocumento().trim().equals(documento.trim()) && u.getRol().equals(RolUsuarioEnum.ADMINISTRADOR)) {
                JOptionPane.showMessageDialog(null,
                        "No puedes eliminar al administrador principal.",
                        "Acceso denegado",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        // Llama al DAO para eliminar el usuario con el documento indicado
    // Devuelve true si se eliminó correctamente, false si hubo algún error
        return dao.eliminarUsuario(documento);
    }


}
