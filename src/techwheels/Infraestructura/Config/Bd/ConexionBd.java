 

package techwheels.Infraestructura.Config.Bd;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Antonio Aguilar
 */
public class ConexionBd {
    private static EntityManagerFactory emf;

    // Método para obtener EntityManagerFactory (solo se crea una vez)
    public static EntityManagerFactory conectar() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("ConfiguracionBd"); // El nombre debe coincidir con persistence.xml
        }
        return emf;
    }

    // Método para cerrar la conexión
    public static void desconectar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        emf = null;
    }

    // Método para obtener EntityManager 
    public static EntityManager getEntityManager() {
        if (emf == null) {
            conectar(); // Asegura que emf esté inicializado
        }
        return emf.createEntityManager();
    }
}