/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package techwheels.Interfaces;

import Controller.Sesion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import techwheels.Clases.CarritoTemp;
import techwheels.Clases.Compra;
import techwheels.Clases.Enumeraciones.GenerarFactura;
import techwheels.Clases.Usuario;
import techwheels.DAO.CompraDAO;

/**
 *
 * @author ASUS
 */
public class HistorialCompras extends javax.swing.JFrame {

    /**
     * Creates new form HistorialCompras
     */
    private DefaultTableModel modelo;
    
    public HistorialCompras() {
        initComponents();
         configurarTabla();
         cargarTablaCompras();
    }
    
    private void configurarTabla() {

    String[] columnas = {
        "ID", "Cliente", "Documento", "Método Pago", "Fecha",
        "Total", "Productos", "Acciones"
    };

    modelo = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 6 || col == 7; // Solo botones
        }
    };

    tabla.setModel(modelo);
    tabla.setRowHeight(30);

    JTableHeader header = tabla.getTableHeader();
    header.setBackground(new Color(0, 90, 170));
    header.setForeground(Color.WHITE);
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));

    // filas alternadas
    tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(row % 2 == 0
                        ? new Color(235, 240, 255)
                        : Color.WHITE);
            }

            return c;
        }
    });

    tabla.getColumn("Productos").setCellRenderer(new ButtonRenderer());
    tabla.getColumn("Productos").setCellEditor(new ButtonEditor(new JTextField(), "Ver"));

    tabla.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
    tabla.getColumn("Acciones").setCellEditor(new ButtonEditorAcciones(new JTextField()));
}
    
     private void cargarTablaCompras() {
        CompraDAO dao = new CompraDAO();

        Usuario u = Sesion.usuarioActual;

        if (u == null) {
            JOptionPane.showMessageDialog(this, 
                "Error: No hay usuario en sesión.", 
                "Sesión no encontrada", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
            // Cargar SOLO las compras del usuario logueado
          List<Compra> compras = dao.cargarComprasDeUsuario();

 
        modelo.setRowCount(0);

        for (Compra c : compras) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNombreCliente() + " " + c.getApellidoCliente(),
                c.getTipoDocumento() + " " + c.getNumeroDocumento(),
                c.getMetodoPago(),
                c.getFechaCompra(),
                c.getTotal(),
                "Ver",
                "PDF / Cancelar"
            });
        }

        //agregarBotonesTabla();
    }
     
    private void mostrarProductos(String idCompra) {
        
        
        CompraDAO dao = new CompraDAO();
        Compra compra = dao.buscarCompraPorId(idCompra);

        if (compra == null) {
            JOptionPane.showMessageDialog(this, "Compra no encontrada.");
            return;
        }

        JDialog dialog = new JDialog(this, "Productos de la compra " + idCompra, true);
        dialog.setSize(600, 300);
        dialog.setLocationRelativeTo(this);

        String[] cols = {"Producto", "Marca", "Categoría", "Precio", "Cantidad"};
        DefaultTableModel modelProd = new DefaultTableModel(cols, 0);

        for (CarritoTemp p : compra.getProductos()) {
            modelProd.addRow(new Object[]{
                p.getNombreProducto(),
                p.getMarcaProducto(),
                p.getCategoriaProducto(),
                p.getPrecioProducto(),
                p.getCantidad()
            });
        }

        JTable tablaProd = new JTable(modelProd);
        tablaProd.setRowHeight(25);

        dialog.add(new JScrollPane(tablaProd));
        dialog.setVisible(true);
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            setText(value.toString());
            setForeground(Color.WHITE);

            if (value.equals("Ver")) {
                setBackground(new Color(0, 120, 215));
            } else {
                setBackground(new Color(200, 0, 0));
            }

            return this;
        }
    }
     
    class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private String label;
        private boolean clicked;
        private int row;

        public ButtonEditor(JTextField text, String label) {
            super(text);
            this.label = label;

            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {

            this.row = row;
            button.setText(label);
            button.setBackground(new Color(0, 120, 215));
            button.setForeground(Color.WHITE);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                String idCompra = tabla.getValueAt(row, 0).toString();
                mostrarProductos(idCompra);
            }
            clicked = false;
            return label;
        }
    } 
    
    class ButtonEditorAcciones extends DefaultCellEditor {

        private JPanel panel;
        private JButton btnPDF, btnCancelar;
        private int row;
        private boolean pdfClicked = false;
        private boolean cancelClicked = false;

        public ButtonEditorAcciones(JTextField txt) {
            super(txt);
            panel = new JPanel(new GridLayout(1, 2, 5, 5));

            btnPDF = new JButton("PDF");
            btnPDF.setBackground(new Color(0, 160, 230));
            btnPDF.setForeground(Color.WHITE);

            btnCancelar = new JButton("X");
            btnCancelar.setBackground(new Color(200, 0, 0));
            btnCancelar.setForeground(Color.WHITE);

            panel.add(btnPDF);
            panel.add(btnCancelar);

            // --- ESTA ES LA CLAVE ---
            btnPDF.addActionListener(e -> {
                pdfClicked = true;
                cancelClicked = false;
                fireEditingStopped();
            });

            btnCancelar.addActionListener(e -> {
                cancelClicked = true;
                pdfClicked = false;
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int col) {
            this.row = row;
            pdfClicked = false;
            cancelClicked = false;
            return panel;
        }
        
        
        @Override
        public Object getCellEditorValue() {

            String id = tabla.getValueAt(row, 0).toString();
            CompraDAO dao = new CompraDAO();

            if (pdfClicked) {
            Compra compra = dao.buscarCompraPorId(id);

            if (compra != null) {
                GenerarFactura.generarFacturaPDF(compra);
                JOptionPane.showMessageDialog(null, "PDF generado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró la compra.");
            }
        }

            // ------------ CANCELAR ----------------
            if (cancelClicked) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "¿Seguro que deseas cancelar esta compra?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    dao.cancelarCompra(id);
                    cargarTablaCompras();
                }
            }

            return "PDF / Cancelar";
        }
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
        BtnSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(247, 255, 252));

        BtnSalir.setText("Salir");
        BtnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSalirActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(tabla);

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel1.setText("Historial de Compras");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnSalir)
                .addGap(34, 34, 34))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 838, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnSalir)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

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

    private void BtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSalirActionPerformed
         new Cliente().setVisible(true);
         this.dispose();
// TODO add your handling code here:
    }//GEN-LAST:event_BtnSalirActionPerformed

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
            java.util.logging.Logger.getLogger(HistorialCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HistorialCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HistorialCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HistorialCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HistorialCompras().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
