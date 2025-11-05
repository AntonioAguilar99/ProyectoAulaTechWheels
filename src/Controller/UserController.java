/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import techwheels.Clases.Usuario;
import techwheels.DAO.UsuarioDAO;

/**
 *
 * @author ASUS
 */
public class UserController {
      private UsuarioDAO userDAO;
    
   public UserController() {
        this.userDAO = new UsuarioDAO();
    }
    
   public List<Usuario> listarUsuarios() {
        return userDAO.listarUsuarios();
    }
    
    public Usuario login(String correo, String contraseña) {
        List<Usuario> user = userDAO.listarUsuarios();

        for (Usuario usuarios: user) {
            if (usuarios.getCorreo().equalsIgnoreCase(correo) &&
                usuarios.getContraseña().equals(contraseña)) {
                return usuarios; // login exitoso
            }
        }
        return null; // no encontrado
    }
    
     public boolean registrarUsuario(Usuario nuevoUsuario) {
        List<Usuario> usuarios = userDAO.listarUsuarios();

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
          
  
        usuarios.add(nuevoUsuario);
        userDAO.guardarUsuarios(usuarios);
        JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");
        return true;
    }
     
     public void cargarUsuarios(DefaultTableModel u){
         List<Usuario> usuario = userDAO.listarUsuarios();
         System.out.println("Usuarios cargados: " + usuario.size());
        for (Usuario user : usuario) {//recorre todos los elementos de la lista reservas.Se toma un objeto reserva de la lista
            Object[] fila = {//Se crea un arreglo de objetos. Con los datos de la reserva
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
}
