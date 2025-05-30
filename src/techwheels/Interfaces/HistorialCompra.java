/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package techwheels.Interfaces;

import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import techwheels.Clases.CarritoTemp;
import techwheels.Infraestructura.Config.Bd.ConexionBd;

/**
 *
 * @author anton
 */
public class HistorialCompra extends javax.swing.JFrame {

    /**
     * Creates new form VenderProducto
     */
    public HistorialCompra() {
        initComponents();
        setLocationRelativeTo(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/cooltext482257288908357.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 57, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/llave-inglesa.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 21, 131, -1));

        jButton1.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/visibility.png"))); // NOI18N
        jButton1.setText("Buscar Historial");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 314, -1, -1));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable4);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 155, 720, -1));

        jButton2.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/exit.png"))); // NOI18N
        jButton2.setText("Salir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 529, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel modelo = new DefaultTableModel(
    new Object[]{ 
        "Nombre Producto", "Descripción", "Precio", "Cantidad", 
        "Método de Pago", "Nombre Cliente", "Tipo Documento", 
        "Número Documento", "Fecha", "Subtotal", "Total" 
    }, 0
    );
    jTable4.setModel(modelo);

    // Ajustar tamaño preferido por columna
    jTable4.getColumnModel().getColumn(0).setPreferredWidth(150); // Nombre Producto
    jTable4.getColumnModel().getColumn(1).setPreferredWidth(300); // Descripción
    jTable4.getColumnModel().getColumn(2).setPreferredWidth(80);  // Precio
    jTable4.getColumnModel().getColumn(3).setPreferredWidth(70);  // Cantidad
    jTable4.getColumnModel().getColumn(4).setPreferredWidth(120); // Método de Pago
    jTable4.getColumnModel().getColumn(5).setPreferredWidth(150); // Nombre Cliente
    jTable4.getColumnModel().getColumn(6).setPreferredWidth(100); // Tipo Documento
    jTable4.getColumnModel().getColumn(7).setPreferredWidth(130); // Número Documento
    jTable4.getColumnModel().getColumn(8).setPreferredWidth(100); // Fecha
    jTable4.getColumnModel().getColumn(9).setPreferredWidth(100); // Subtotal
    jTable4.getColumnModel().getColumn(10).setPreferredWidth(100); // Total

    // Opcional: ajustar altura si el texto es largo
    jTable4.setRowHeight(30);

    // Opcional: permitir ajuste automático al cambiar tamaño
    jTable4.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    // Pedir número de documento
    String cedula = JOptionPane.showInputDialog(null, "Ingrese su número de cédula para ver el carrito:");

  if (cedula != null && !cedula.trim().isEmpty()) {
    EntityManager em = ConexionBd.conectar().createEntityManager();

    try {
        List<CarritoTemp> lista = em.createQuery(
            "SELECT c FROM CarritoTemp c WHERE c.numeroDocumento = :cedula", CarritoTemp.class)
            .setParameter("cedula", cedula)
            .getResultList();

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron productos para la cédula ingresada.");
        } else {
            for (CarritoTemp c : lista) {
                modelo.addRow(new Object[]{
                    c.getNombreProducto(),
                    c.getDescripcionProducto(),
                    c.getPrecioProducto(),
                    c.getCantidad(),
                    c.getMetodoPago(),
                    c.getNombreCliente(),
                    c.getTipoDocumento(),
                    c.getNumeroDocumento(),
                    c.getFecha(),
                    c.getSubtotal(),
                    c.getTotal()
                });
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al cargar el carrito: " + e.getMessage());
    } finally {
        em.close();
    }

} else {
    JOptionPane.showMessageDialog(null, "Debe ingresar un número de cédula válido.");
}

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new Cliente().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HistorialCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HistorialCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HistorialCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HistorialCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HistorialCompra().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable4;
    // End of variables declaration//GEN-END:variables
}
