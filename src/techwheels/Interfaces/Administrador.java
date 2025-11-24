/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package techwheels.Interfaces;

import Controller.InventarioController;
import Controller.Sesion;
import Controller.UserController;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import techwheels.Clases.CarritoTemp;
import techwheels.Clases.Compra;
import techwheels.Clases.Enumeraciones.GenerarFactura;
import techwheels.Clases.Enumeraciones.RolUsuarioEnum;

import techwheels.Clases.GestionProductos;
import techwheels.Clases.Usuario;
import techwheels.DAO.CarritoDAO;
import techwheels.DAO.CompraDAO;
import techwheels.DAO.ProductosDAO;
import techwheels.DAO.UsuarioDAO;

/**
 *
 * @author anton
 */
public class Administrador extends javax.swing.JFrame {
    private String numeroDocumentoOriginal;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private UserController controller = new UserController();
    private DefaultTableModel modelo;
    private final ProductosDAO dao = new ProductosDAO();
    private Usuario usuarioActual;
    private Compra compraActual;
    private TableRowSorter<DefaultTableModel> sorter;
    private DefaultTableModel modeloHistorial;

    private CarritoDAO carritoDAO = new CarritoDAO();

    /**
     * Creates new form Administrador
     */

    public Administrador() {
        initComponents();
        modelo = (DefaultTableModel) tablaUsuarios.getModel();
        modeloHistorial = (DefaultTableModel) tablaHistorial.getModel();
        sorter = new TableRowSorter<>(modelo);
        tablaUsuarios.setRowSorter(sorter);
        cargarUsers();
        configurarTabla();
        configurarTabla1();
        cargarTablaCompras();
    
        

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila >= 0) {
                txtNombre.setText(tablaProductos.getValueAt(fila, 2).toString());
                txtEntregadoPor.setText(tablaProductos.getValueAt(fila, 3).toString());
                txtRecibidoPor.setText(tablaProductos.getValueAt(fila, 4).toString());
                txtDescripcion.setText(tablaProductos.getValueAt(fila, 5).toString());
                txtMarca.setText(tablaProductos.getValueAt(fila, 6).toString());
                txtCategoria.setText(tablaProductos.getValueAt(fila, 7).toString());
                txtCantidad.setText(tablaProductos.getValueAt(fila, 8).toString());
                txtPrecio.setText(tablaProductos.getValueAt(fila, 9).toString());
            }
        });

    }

    public Administrador(Usuario usuario) {
        initComponents();
        setLocationRelativeTo(this);
        this.usuarioActual = usuario;
        configurarTabla1();
        cargarTablaCompras();
    

    }
    
    //METODO PARA CARGAR USUARIOS
    public void cargarUsers() {
        DefaultTableModel userModel = (DefaultTableModel) tablaUsuarios.getModel();
        userModel.setRowCount(0);
        controller.cargarUsuarios(userModel);

    }
    
    private void filtrarUsuario() {
        if (sorter == null) {
            // Inicializar sorter si es nulo
            DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
            sorter = new TableRowSorter<>(modelo);
            tablaUsuarios.setRowSorter(sorter);
        }

        String doc = txtBuscarDocumento.getText().trim();

        if (doc.isEmpty()) {
            sorter.setRowFilter(null); // quitar filtro
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + doc, 4));
            // 4 es el índice de columna Documento (ajusta si es otro)
        }
    }

    private void eliminarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar");
            return;
        }
        // Obtener el documento desde la tabla
        String documento = tablaUsuarios.getValueAt(fila, 4).toString().trim();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de eliminar este usuario?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        // Llamar a CONTROLLER
        boolean eliminado = controller.eliminarUsuario(documento);

        if (eliminado) {
            JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente");

            // Recargar tabla
            DefaultTableModel model = (DefaultTableModel) tablaUsuarios.getModel();
            
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el usuario.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    // ✅ Configurar tabla para que tenga scroll y columnas correctas
    private void configurarTabla() {
        String[] columnas = {"ID", "Fecha", "Nombre del producto", "Entregado por", "Recibido por", "Descripción", "Marca", "Categoria", "Cantidad", "Precio"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tablaProductos.setModel(modelo);
        tablaProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // para usar scrollbars

        // Ajustar ancho de cada columna
        int[] anchos = {50, 125, 200, 150, 150, 450, 100, 100, 100, 100};
        for (int i = 0; i < tablaProductos.getColumnCount(); i++) {
            tablaProductos.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        // Ajustar altura de filas
        tablaProductos.setRowHeight(30); // más grande, por defecto es 16 o 20
    }


    private void mostrarProductos() {
    List<GestionProductos> productos = dao.listarProductos();
    DefaultTableModel modeloProductos = (DefaultTableModel) tablaProductos.getModel();
    modeloProductos.setRowCount(0); // Limpiar tabla antes de mostrar

    for (GestionProductos p : productos) {
        modeloProductos.addRow(new Object[]{
            p.getId(),
            p.getFecha(),
            p.getNombre(),
            p.getEntregadoPor(),
            p.getRecibidoPor(),
            p.getDescripcion(),
            p.getMarca(),
            p.getCategoria(),
            p.getCantidad(),
            p.getPrecio()
        });
    }
}
  
    
     public void cargarProductos() {
        DefaultTableModel product = (DefaultTableModel) TablaProductos.getModel();
        product.setRowCount(0);
        InventarioController inventario = new InventarioController();
        inventario.cargarProductos(product);

    }
    
     private void mostrarCalendario() {
     JDateChooser datechooser = new JDateChooser();
     datechooser.setDateFormatString("dd/MM/yyyy");
     
     int opcion = JOptionPane.showConfirmDialog(
         this,
         datechooser,
         "Seleccione una fecha",
         JOptionPane.OK_CANCEL_OPTION,
         JOptionPane.PLAIN_MESSAGE
     
     
     );
     
     if(opcion == JOptionPane.OK_OPTION){
         Date fechaseleccionada = datechooser.getDate();
         if(fechaseleccionada !=null){
             SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
             txtFecha.setText(formato.format(fechaseleccionada));
         }else{
            JOptionPane.showMessageDialog(this, "No seleccionaste ninguna fecha");
         }
             
     }
        
    } 
    
    private void mostrarCarrito() {
    List<CarritoTemp> carrito = carritoDAO.listarcarrito();
    DefaultTableModel modelo = new DefaultTableModel(
        new Object[]{"Nombre", "Descripción", "Marca", "Categoría", "Precio", "Cantidad"}, 0
    );

    for (CarritoTemp c : carrito) {
        modelo.addRow(new Object[]{
            c.getNombreProducto(),
            c.getDescripcionProducto(),
            c.getMarcaProducto(),
            c.getCategoriaProducto(),
            c.getPrecioProducto(),
            c.getCantidad()
        });
    }

    TablaProductos.setModel(modelo);
}
    
    
//Desde aqui hasta abajo todo es sobre la interfaz historial y cancelacion de compras 
private void configurarTabla1() {

    String[] columnas = {
        "ID", "Cliente", "Documento", "Método Pago", "Fecha",
        "Total","Estado", "Productos", "Acciones"
    };

    modeloHistorial = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 7 || col == 8; // Solo botones
        }
    };

    tablaHistorial.setModel(modeloHistorial);
    tablaHistorial.setRowHeight(30);

    JTableHeader header = tablaHistorial.getTableHeader();
    header.setBackground(new Color(0, 90, 170));
    header.setForeground(Color.WHITE);
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));

    // filas alternadas
    tablaHistorial.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

    tablaHistorial.getColumn("Productos").setCellRenderer(new ButtonRenderer());
    tablaHistorial.getColumn("Productos").setCellEditor(new ButtonEditor(new JTextField(), "Ver"));

    tablaHistorial.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
    tablaHistorial.getColumn("Acciones").setCellEditor(new ButtonEditorAcciones(new JTextField()));
}

private void cargarTablaCompras() {

    CompraDAO dao = new CompraDAO();

    // Cargar TODAS las compras, no solo las del usuario
    List<Compra> compras = dao.cargarCompras1();

    modeloHistorial.setRowCount(0);

    for (Compra c : compras) {
        modeloHistorial.addRow(new Object[]{
            c.getId(),
            c.getNombreCliente() + " " + c.getApellidoCliente(),
            c.getTipoDocumento() + " " + c.getNumeroDocumento(),
            c.getMetodoPago(),
            c.getFechaCompra(),
            c.getTotal(),
            c.getEstado(),
            "Ver",
            "PDF / Cancelar"
        });
    }
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
            String idCompra = tablaHistorial.getValueAt(row, 0).toString();
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

        String id = tablaHistorial.getValueAt(row, 0).toString();
        CompraDAO dao = new CompraDAO();

        // ------------ PDF ----------------
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
    // Método para refrescar tabla
    private void refrescarTabla() {
        CompraDAO dao = new CompraDAO();
        List<Compra> compras = dao.cargarCompras1();

        DefaultTableModel modelo = (DefaultTableModel) tablaHistorial.getModel();
        modelo.setRowCount(0); // limpia la tabla

        for (Compra c : compras) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNombreCliente() + " " + c.getApellidoCliente(),
                c.getNumeroDocumento(),
                c.getMetodoPago(),
                c.getFechaCompra(),
                c.getTotal(),
                "Ver",
                "PDF / Cancelar"
            });
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

        Administrador = new javax.swing.JTabbedPane();
        Usuarios = new javax.swing.JPanel();
        btnMostrarUsuarios = new javax.swing.JButton();
        btnEliminarUsuario = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        btnSalir = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtBuscarDocumento = new javax.swing.JTextField();
        btnBuscarUser = new javax.swing.JButton();
        Registro = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        NombreTxt = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        ComboDocumento = new javax.swing.JComboBox<>();
        BtnRegistrar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        ApellidoTxt = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        txtNumeroDocumento = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        ComboRol = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        TelefonoTxt = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        setContrasena = new javax.swing.JPasswordField();
        jSeparator6 = new javax.swing.JSeparator();
        chkMostrar = new javax.swing.JCheckBox();
        RealizarCompra = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablaProductos = new javax.swing.JTable();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        textNombre = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        ComboMetodoPago = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        textApellidos = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        btnFecha = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        spinnerCantidad = new javax.swing.JSpinner();
        ComboTD = new javax.swing.JComboBox<>();
        CancelarCompra = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaHistorial = new javax.swing.JTable();
        btnBuscarCedula = new javax.swing.JButton();
        btnCargar = new javax.swing.JButton();
        HistorialCompras = new javax.swing.JPanel();
        BuscarUsuarioCedula = new javax.swing.JButton();
        ModificarPerfilUsuario = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        N = new javax.swing.JTextField();
        ND = new javax.swing.JTextField();
        A = new javax.swing.JTextField();
        CE = new javax.swing.JTextField();
        NC = new javax.swing.JTextField();
        C = new javax.swing.JPasswordField();
        TD = new javax.swing.JComboBox<>();
        TU = new javax.swing.JComboBox<>();
        M = new javax.swing.JCheckBox();
        jButton4 = new javax.swing.JButton();
        GestionProductos = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        btnGuardarProducto = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        txtEntregadoPor = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        txtRecibidoPor = new javax.swing.JTextField();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        txtCategoria = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        txtMarca = new javax.swing.JTextField();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Usuarios.setBackground(new java.awt.Color(200, 225, 220));

        btnMostrarUsuarios.setFont(new java.awt.Font("Segoe UI Symbol", 0, 12)); // NOI18N
        btnMostrarUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/visibility.png"))); // NOI18N
        btnMostrarUsuarios.setText("Mostrar Usuarios");
        btnMostrarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarUsuariosActionPerformed(evt);
            }
        });

        btnEliminarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/cancelled.png"))); // NOI18N
        btnEliminarUsuario.setText("Eliminar Usuario");
        btnEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUsuarioActionPerformed(evt);
            }
        });

        tablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Apellido", "T. Documento", "Num. Documento", "Correo", "Telefono", "Contraseña", "Rol"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaUsuarios);
        if (tablaUsuarios.getColumnModel().getColumnCount() > 0) {
            tablaUsuarios.getColumnModel().getColumn(0).setResizable(false);
            tablaUsuarios.getColumnModel().getColumn(1).setResizable(false);
            tablaUsuarios.getColumnModel().getColumn(2).setResizable(false);
            tablaUsuarios.getColumnModel().getColumn(3).setResizable(false);
            tablaUsuarios.getColumnModel().getColumn(4).setResizable(false);
            tablaUsuarios.getColumnModel().getColumn(5).setResizable(false);
            tablaUsuarios.getColumnModel().getColumn(6).setResizable(false);
            tablaUsuarios.getColumnModel().getColumn(7).setResizable(false);
            tablaUsuarios.getColumnModel().getColumn(8).setResizable(false);
        }

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/exit.png"))); // NOI18N
        btnSalir.setText("Salir\n");
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalirMouseClicked(evt);
            }
        });
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/refresh.png"))); // NOI18N
        btnRefrescar.setText("Refrescar");
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 30)); // NOI18N
        jLabel11.setText("Visualice facil y rapido los usuarios registrados en el programa");

        btnBuscarUser.setText("Buscar");
        btnBuscarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UsuariosLayout = new javax.swing.GroupLayout(Usuarios);
        Usuarios.setLayout(UsuariosLayout);
        UsuariosLayout.setHorizontalGroup(
            UsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsuariosLayout.createSequentialGroup()
                .addGroup(UsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UsuariosLayout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addGroup(UsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 1091, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(UsuariosLayout.createSequentialGroup()
                                .addComponent(btnSalir)
                                .addGap(87, 87, 87)
                                .addComponent(btnMostrarUsuarios)
                                .addGap(90, 90, 90)
                                .addComponent(btnRefrescar)
                                .addGap(45, 45, 45)
                                .addComponent(btnEliminarUsuario))))
                    .addGroup(UsuariosLayout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(btnBuscarUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(327, Short.MAX_VALUE))
        );
        UsuariosLayout.setVerticalGroup(
            UsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsuariosLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel11)
                .addGap(49, 49, 49)
                .addGroup(UsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(UsuariosLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtBuscarDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                    .addComponent(btnBuscarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(UsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarUsuario)
                    .addComponent(btnRefrescar)
                    .addComponent(btnMostrarUsuarios)
                    .addComponent(btnSalir))
                .addContainerGap(164, Short.MAX_VALUE))
        );

        Administrador.addTab("Usuarios", Usuarios);

        Registro.setBackground(new java.awt.Color(200, 225, 220));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel1.setText("Registre a los Usuarios Facil y Rapido");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Nombres");

        NombreTxt.setBorder(null);

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Tipo de Documento");

        ComboDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "C.C", "Cedula de Extranjeria" }));
        ComboDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboDocumentoActionPerformed(evt);
            }
        });

        BtnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/register.png"))); // NOI18N
        BtnRegistrar.setText("Registrar");
        BtnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRegistrarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Apellidos");

        ApellidoTxt.setBorder(null);
        ApellidoTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApellidoTxtActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Correo Electronico");

        txtCorreo.setBorder(null);

        jLabel6.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Numero de Documento");

        txtNumeroDocumento.setBorder(null);
        txtNumeroDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDocumentoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Tipo de Usuario");

        ComboRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "ADMINISTRADOR", "CLIENTE" }));

        jLabel7.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Numero de Celular");

        TelefonoTxt.setBorder(null);
        TelefonoTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TelefonoTxtActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Contraseña");

        setContrasena.setBorder(null);

        chkMostrar.setText("Mostrar");
        chkMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMostrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(NombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(TelefonoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ComboDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(79, 79, 79))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(257, 257, 257)
                                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ApellidoTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(226, 226, 226)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNumeroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ComboRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(setContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chkMostrar))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(20, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(348, 348, 348)
                .addComponent(BtnRegistrar)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ApellidoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TelefonoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkMostrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(BtnRegistrar))
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );

        javax.swing.GroupLayout RegistroLayout = new javax.swing.GroupLayout(Registro);
        Registro.setLayout(RegistroLayout);
        RegistroLayout.setHorizontalGroup(
            RegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RegistroLayout.createSequentialGroup()
                .addGroup(RegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RegistroLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(RegistroLayout.createSequentialGroup()
                        .addGap(327, 327, 327)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(441, Short.MAX_VALUE))
        );
        RegistroLayout.setVerticalGroup(
            RegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RegistroLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(37, 37, 37)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(150, Short.MAX_VALUE))
        );

        Administrador.addTab("Registrar", Registro);

        RealizarCompra.setBackground(new java.awt.Color(247, 250, 252));

        jButton13.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton13.setText("Cargar Productos");
        jButton13.setBorder(new javax.swing.border.MatteBorder(null));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(192, 221, 245));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/shopping-cart.png"))); // NOI18N
        jButton14.setText("Agregar al carrito");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(0, 120, 215));
        jButton15.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("Realizar Compra");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        TablaProductos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));
        TablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Descripcion", "Marca", "Categoria", "Precio", "Disponible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaProductos.setPreferredSize(new java.awt.Dimension(450, 620));
        jScrollPane5.setViewportView(TablaProductos);

        jButton17.setBackground(new java.awt.Color(192, 221, 245));
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/shopping-cart.png"))); // NOI18N
        jButton17.setText("Ver Carrito");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(192, 221, 245));
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/cancelled.png"))); // NOI18N
        jButton18.setText("Quitar del carrito");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel29.setText("Nombres");

        textNombre.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        textNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));

        jLabel30.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel30.setText("Tipo de documento");

        jLabel31.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel31.setText("Numero de documento");

        txtNumero.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtNumero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));
        txtNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel32.setText("Metodo de pago");

        ComboMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona", "Efectivo", "Contra Entrega", "Tarjeta de Debito", "Tarjeta de Credito", " " }));

        jLabel33.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel33.setText("Fecha");

        jLabel21.setFont(new java.awt.Font("SansSerif", 1, 40)); // NOI18N
        jLabel21.setText("TECHWHEELS");

        jLabel22.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel22.setText("Apellidos");

        textApellidos.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        textApellidos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));

        txtFecha.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtFecha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));
        txtFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaActionPerformed(evt);
            }
        });

        btnFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/visibility.png"))); // NOI18N
        btnFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechaActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel23.setText("Direccion de envio");

        txtDireccion.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtDireccion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));

        jButton3.setBackground(new java.awt.Color(232, 234, 236));
        jButton3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(102, 102, 102));
        jButton3.setText("Descargar Factura");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel28.setText("Cantidad");

        spinnerCantidad.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

        ComboTD.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona", "C.C", "Cedula de extranjeria" }));

        javax.swing.GroupLayout RealizarCompraLayout = new javax.swing.GroupLayout(RealizarCompra);
        RealizarCompra.setLayout(RealizarCompraLayout);
        RealizarCompraLayout.setHorizontalGroup(
            RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RealizarCompraLayout.createSequentialGroup()
                .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(RealizarCompraLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(RealizarCompraLayout.createSequentialGroup()
                                .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel30)
                                    .addComponent(ComboMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ComboTD, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(95, 95, 95)
                                .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(RealizarCompraLayout.createSequentialGroup()
                                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnFecha))))
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDireccion)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, RealizarCompraLayout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RealizarCompraLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(RealizarCompraLayout.createSequentialGroup()
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(RealizarCompraLayout.createSequentialGroup()
                                .addGap(166, 166, 166)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(RealizarCompraLayout.createSequentialGroup()
                                .addGap(154, 154, 154)
                                .addComponent(spinnerCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(80, 80, 80)
                        .addComponent(jButton14)
                        .addGap(54, 54, 54)
                        .addComponent(jButton18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(101, 101, 101))
            .addGroup(RealizarCompraLayout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        RealizarCompraLayout.setVerticalGroup(
            RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RealizarCompraLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RealizarCompraLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(49, 49, 49)
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ComboTD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(RealizarCompraLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                                .addComponent(jLabel33)
                                .addGap(6, 6, 6)
                                .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(RealizarCompraLayout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ComboMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(57, 57, 57)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(147, Short.MAX_VALUE))
                    .addGroup(RealizarCompraLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(RealizarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(RealizarCompraLayout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spinnerCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        Administrador.addTab("Realizar Compra", RealizarCompra);

        CancelarCompra.setBackground(new java.awt.Color(200, 225, 220));

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel10.setText("BUSQUEDA DE HISTORIALES Y CANCELACION DE COMPRAS ");

        tablaHistorial.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jScrollPane2.setViewportView(tablaHistorial);

        btnBuscarCedula.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/visibility.png"))); // NOI18N
        btnBuscarCedula.setText("Buscar por Cedula");
        btnBuscarCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCedulaActionPerformed(evt);
            }
        });

        btnCargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/refresh.png"))); // NOI18N
        btnCargar.setText("Refrescar Tabla");
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CancelarCompraLayout = new javax.swing.GroupLayout(CancelarCompra);
        CancelarCompra.setLayout(CancelarCompraLayout);
        CancelarCompraLayout.setHorizontalGroup(
            CancelarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CancelarCompraLayout.createSequentialGroup()
                .addGroup(CancelarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CancelarCompraLayout.createSequentialGroup()
                        .addGap(477, 477, 477)
                        .addComponent(btnBuscarCedula)
                        .addGap(156, 156, 156)
                        .addComponent(btnCargar))
                    .addGroup(CancelarCompraLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jLabel10))
                    .addGroup(CancelarCompraLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1455, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        CancelarCompraLayout.setVerticalGroup(
            CancelarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CancelarCompraLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(CancelarCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarCedula)
                    .addComponent(btnCargar))
                .addGap(153, 153, 153))
        );

        Administrador.addTab("Historial y Cancelacion de Compra", CancelarCompra);

        HistorialCompras.setBackground(new java.awt.Color(200, 225, 220));

        BuscarUsuarioCedula.setBackground(new java.awt.Color(204, 255, 255));
        BuscarUsuarioCedula.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BuscarUsuarioCedula.setForeground(new java.awt.Color(51, 51, 51));
        BuscarUsuarioCedula.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/visibility.png"))); // NOI18N
        BuscarUsuarioCedula.setText("Buscar Usuario por Cedula");
        BuscarUsuarioCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarUsuarioCedulaActionPerformed(evt);
            }
        });

        ModificarPerfilUsuario.setBackground(new java.awt.Color(204, 255, 255));
        ModificarPerfilUsuario.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        ModificarPerfilUsuario.setForeground(new java.awt.Color(51, 51, 51));
        ModificarPerfilUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/order.png"))); // NOI18N
        ModificarPerfilUsuario.setText("Modificar perfil de Usuario");
        ModificarPerfilUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarPerfilUsuarioActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel24.setText("MODIFIQUE LA INFORMACION DE LOS USUARIOS FACIL Y RAPIDO");

        jPanel3.setBackground(new java.awt.Color(247, 250, 252));

        jLabel25.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel25.setText("Nombres");

        jLabel26.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel26.setText("Tipo de Documento");

        jLabel27.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel27.setText("Apellidos");

        jLabel34.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel34.setText("Numero de Documento");

        jLabel35.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel35.setText("Correo Electronico");

        jLabel36.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel36.setText("Tipo de Usuario");

        jLabel37.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel37.setText("Numero de Celular");

        jLabel38.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel38.setText("Contraseña");

        N.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        N.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));
        N.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NActionPerformed(evt);
            }
        });

        ND.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        ND.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));

        A.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        A.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));

        CE.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        CE.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));

        NC.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        NC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));

        C.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        C.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(229, 223, 223)));

        TD.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        TD.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "C.C", "Cedula de Extranjeria" }));

        TU.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        TU.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "ADMINISTRADOR", "CLIENTE" }));

        M.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        M.setText("Mostrar");
        M.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(NC)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(N, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(A)
                    .addComponent(CE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 180, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(150, 150, 150))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TD, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ND, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TU, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(C, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(M)))
                        .addGap(22, 22, 22))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(N, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TD, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(92, 92, 92)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(A, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ND, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CE, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TU, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(NC, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(C)
                    .addComponent(M, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(85, 85, 85))
        );

        jButton4.setBackground(new java.awt.Color(204, 255, 255));
        jButton4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(51, 51, 51));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/refresh.png"))); // NOI18N
        jButton4.setText("Limpiar Campos");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HistorialComprasLayout = new javax.swing.GroupLayout(HistorialCompras);
        HistorialCompras.setLayout(HistorialComprasLayout);
        HistorialComprasLayout.setHorizontalGroup(
            HistorialComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistorialComprasLayout.createSequentialGroup()
                .addGroup(HistorialComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(HistorialComprasLayout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(HistorialComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(BuscarUsuarioCedula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ModificarPerfilUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, HistorialComprasLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 1254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 275, Short.MAX_VALUE))
        );
        HistorialComprasLayout.setVerticalGroup(
            HistorialComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistorialComprasLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel24)
                .addGroup(HistorialComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HistorialComprasLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(BuscarUsuarioCedula)
                        .addGap(129, 129, 129)
                        .addComponent(ModificarPerfilUsuario)
                        .addGap(125, 125, 125)
                        .addComponent(jButton4))
                    .addGroup(HistorialComprasLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(111, Short.MAX_VALUE))
        );

        Administrador.addTab("Modificacion de Perfil de Usuario", HistorialCompras);

        GestionProductos.setBackground(new java.awt.Color(192, 221, 245));

        jLabel13.setBackground(new java.awt.Color(51, 51, 51));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("SUBA LOS PRODUCTOS FACIL Y RAPIDO");

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Fecha", "Nombre", "Entregado por", "Recibido por", "Descripcion", "Marca", "Categoria", "Cantidad", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tablaProductos);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/visibility.png"))); // NOI18N
        jButton6.setText("Ver Productos");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        btnGuardarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/up-arrow.png"))); // NOI18N
        btnGuardarProducto.setText("Subir");
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/order.png"))); // NOI18N
        jButton8.setText("Modificar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/cancelled.png"))); // NOI18N
        jButton9.setText("Eliminar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("Nombre del producto");

        txtNombre.setBorder(null);

        txtEntregadoPor.setBorder(null);

        jLabel18.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("Entregado por");

        txtRecibidoPor.setBorder(null);

        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("Recibido por");

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("Descripcion");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane3.setViewportView(txtDescripcion);

        jLabel16.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Categoria");

        txtCantidad.setBorder(null);

        txtCategoria.setBorder(null);

        txtPrecio.setBorder(null);

        jLabel19.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setText("Precio");

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Cantidad");

        jLabel17.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Marca");

        txtMarca.setBorder(null);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtRecibidoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNombre)
                                .addComponent(jSeparator8)
                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(113, 113, 113))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEntregadoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCategoria)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jSeparator9)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 10, Short.MAX_VALUE)))
                        .addGap(85, 85, 85))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jSeparator15))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(76, 76, 76)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEntregadoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(92, 92, 92)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRecibidoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel15)))
                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/techwheels/Imagenes/refresh.png"))); // NOI18N
        jButton2.setText("Quitar Seleccion de la tabla y limpiar campos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout GestionProductosLayout = new javax.swing.GroupLayout(GestionProductos);
        GestionProductos.setLayout(GestionProductosLayout);
        GestionProductosLayout.setHorizontalGroup(
            GestionProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GestionProductosLayout.createSequentialGroup()
                .addGroup(GestionProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(GestionProductosLayout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GestionProductosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addGroup(GestionProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, GestionProductosLayout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(143, 143, 143)
                        .addComponent(btnGuardarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8)
                        .addGap(52, 52, 52))
                    .addGroup(GestionProductosLayout.createSequentialGroup()
                        .addGap(0, 65, Short.MAX_VALUE)
                        .addGroup(GestionProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(GestionProductosLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 775, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
                            .addGroup(GestionProductosLayout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(70, 70, 70)
                                .addComponent(jButton9)
                                .addGap(188, 188, 188))))))
        );
        GestionProductosLayout.setVerticalGroup(
            GestionProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GestionProductosLayout.createSequentialGroup()
                .addGroup(GestionProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(GestionProductosLayout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addGroup(GestionProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardarProducto)
                            .addComponent(jButton6)
                            .addComponent(jButton8))
                        .addGap(42, 42, 42)
                        .addGroup(GestionProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton9)
                            .addComponent(jButton2)))
                    .addGroup(GestionProductosLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(128, Short.MAX_VALUE))
        );

        Administrador.addTab("Gestion de productos", GestionProductos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Administrador)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Administrador)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRegistrarActionPerformed
 try {
        // Validaciones de campos obligatorios
        if (NombreTxt.getText().trim().isEmpty() ||
            ApellidoTxt.getText().trim().isEmpty() ||
            ComboRol.getSelectedItem() == null ||
            txtNumeroDocumento.getText().trim().isEmpty() ||
           txtCorreo.getText().trim().isEmpty() ||
            setContrasena.getText().trim().isEmpty() ||
            ComboDocumento.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.");
            return;
        }
        
         // Validaciones de formato
        if (!NombreTxt.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(null, "El nombre solo debe contener letras.");
            return;
        }
        if (!ApellidoTxt.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(null, "El apellido solo debe contener letras.");
            return;
        }
        if (!txtNumeroDocumento.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "El número de documento solo debe contener números.");
            return;
        }
        if (txtCorreo.getText().trim().length() > 100) {
            JOptionPane.showMessageDialog(null, "El correo no puede superar los 100 caracteres.");
            return;
        }
        if (TelefonoTxt.getText().trim().length() > 15) {
            JOptionPane.showMessageDialog(null, "El teléfono no puede superar los 15 caracteres.");
            return;
        }
        if (!TelefonoTxt.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "El teléfono solo debe contener números.");
            return;
        }
        if (setContrasena.getText().trim().length() > 70) {
            JOptionPane.showMessageDialog(null, "La contraseña no puede superar los 70 caracteres.");
            return;
        }
        if (!txtCorreo.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(null, "El correo no tiene un formato válido.");
            return;
        }
        
         // Cargar usuarios existentes
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> usuarios = usuarioDAO.listarUsuarios();

        // Validar unicidad de correo y documento
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(txtCorreo.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese correo.");
                return;
            }
            if (u.getNumeroDocumento().equalsIgnoreCase(txtNumeroDocumento.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese número de documento.");
                return;
            }
        }
        
         // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setCodigo(UUID.randomUUID().toString());
        usuario.setNombres(NombreTxt.getText().trim());
        usuario.setApellidos(ApellidoTxt.getText().trim());
        usuario.setTipoDocumento(ComboDocumento.getSelectedItem().toString());
        usuario.setNumeroDocumento(txtNumeroDocumento.getText().trim());
        usuario.setCorreo(txtCorreo.getText().trim());
        usuario.setTelefono(TelefonoTxt.getText().trim());
        usuario.setContraseña(setContrasena.getText().trim());

        // Convertir string a enum (si lo usas)
        String rolSeleccionado = ComboRol.getSelectedItem().toString().toUpperCase();
        try {
            usuario.setRol(RolUsuarioEnum.valueOf(rolSeleccionado));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Rol inválido: " + rolSeleccionado);
            return;
        }

        // Agregar y guardar usuario
        usuarios.add(usuario);
        UserController controller = new UserController();
        controller.registrarUsuario(usuario);

        JOptionPane.showMessageDialog(null, "Registro exitoso.");
        limpiarCampos();
        
        } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al registrar: " + e.getMessage());
    }
   


    }//GEN-LAST:event_BtnRegistrarActionPerformed

    private void btnMostrarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarUsuariosActionPerformed
   
        cargarUsers();

    }//GEN-LAST:event_btnMostrarUsuariosActionPerformed

    private void btnEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUsuarioActionPerformed
        eliminarUsuario();
    }//GEN-LAST:event_btnEliminarUsuarioActionPerformed

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        sorter.setRowFilter(null); // ← limpia el filtro
        controller.refrescarTabla((DefaultTableModel) tablaUsuarios.getModel());

    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
        // TODO add your handling code here:
  try {
    String id = UUID.randomUUID().toString();
    String nombre = txtNombre.getText().trim();
    String entregado = txtEntregadoPor.getText().trim();
    String recibido = txtRecibidoPor.getText().trim();
    String descripcion = txtDescripcion.getText().trim();
    String marca = txtMarca.getText().trim();
    String categoria = txtCategoria.getText().trim();
    String cantidadTxt = txtCantidad.getText().trim();
    String precioTxt = txtPrecio.getText().trim();

    // 🔹 Validar que ningún campo esté vacío
    if (nombre.isEmpty() || entregado.isEmpty() || recibido.isEmpty() || 
        descripcion.isEmpty() || marca.isEmpty() || categoria.isEmpty() ||
        cantidadTxt.isEmpty() || precioTxt.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor completa todos los campos antes de continuar.");
        return;
    }

    // 🔹 Validaciones de formato
    if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
        JOptionPane.showMessageDialog(this, "El nombre solo debe contener letras.");
        return;
    }
    if (!entregado.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
        JOptionPane.showMessageDialog(this, "El campo 'Entregado por' solo debe contener letras.");
        return;
    }
    if (!recibido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
        JOptionPane.showMessageDialog(this, "El campo 'Recibido por' solo debe contener letras.");
        return;
    }
    if (!marca.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+")) {
        JOptionPane.showMessageDialog(this, "La marca solo puede contener letras y números.");
        return;
    }
    if (!categoria.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
        JOptionPane.showMessageDialog(this, "La categoría solo debe contener letras.");
        return;
    }

    // 🔹 Validar cantidad y precio con números válidos
    if (!cantidadTxt.matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "La cantidad solo debe contener números enteros positivos.");
        return;
    }
    if (!precioTxt.matches("\\d+(\\.\\d{1,2})?")) {
        JOptionPane.showMessageDialog(this, "El precio solo debe contener números (puede tener decimales).");
        return;
    }

    int cantidad = Integer.parseInt(cantidadTxt);
    double precio = Double.parseDouble(precioTxt);

    // 🔹 Validar que los valores sean mayores que 0
    if (cantidad <= 0) {
        JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.");
        return;
    }
    if (precio <= 0) {
        JOptionPane.showMessageDialog(this, "El precio debe ser mayor que cero.");
        return;
    }

    // ✅ Si todo está correcto, crear y guardar el producto
    GestionProductos nuevo = new GestionProductos(id, nombre, entregado, recibido,
            descripcion, marca, cantidad, categoria, precio);

    dao.agregarProducto(nuevo);
    mostrarProductos();
    limpiarCampos45();

    JOptionPane.showMessageDialog(this, "Producto agregado correctamente ✅");

} catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(this, "Por favor ingresa números válidos en cantidad y precio.");
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Error al agregar el producto: " + e.getMessage());
}


    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
   mostrarProductos();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
int fila = tablaProductos.getSelectedRow();
if (fila >= 0) {
    try {
        String id = tablaProductos.getValueAt(fila, 0).toString();
        String nombre = txtNombre.getText().trim();
        String entregado = txtEntregadoPor.getText().trim();
        String recibido = txtRecibidoPor.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String marca = txtMarca.getText().trim();
         String categoria = txtCategoria.getText().trim();
        String cantidadTxt = txtCantidad.getText().trim();
        String precioTxt = txtPrecio.getText().trim();

        // 🔹 Validar campos vacíos
        if (nombre.isEmpty() || entregado.isEmpty() || recibido.isEmpty() ||
            descripcion.isEmpty() || marca.isEmpty() || categoria.isEmpty() ||
            cantidadTxt.isEmpty() || precioTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa todos los campos antes de actualizar.");
            return;
        }

        // 🔹 Validaciones de formato
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(this, "El nombre solo debe contener letras.");
            return;
        }
        if (!entregado.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(this, "El campo 'Entregado por' solo debe contener letras.");
            return;
        }
        if (!recibido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(this, "El campo 'Recibido por' solo debe contener letras.");
            return;
        }
        if (!marca.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(this, "La marca solo puede contener letras y números.");
            return;
        }
        if (!categoria.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(this, "La categoría solo debe contener letras.");
            return;
        }

        // 🔹 Validar cantidad y precio
        if (!cantidadTxt.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "La cantidad solo debe contener números enteros positivos.");
            return;
        }
        if (!precioTxt.matches("\\d+(\\.\\d{1,2})?")) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido (puede incluir decimales).");
            return;
        }

        int cantidad = Integer.parseInt(cantidadTxt);
        double precio = Double.parseDouble(precioTxt);

        // 🔹 Validar que los valores sean mayores que 0
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.");
            return;
        }
        if (precio <= 0) {
            JOptionPane.showMessageDialog(this, "El precio debe ser mayor que cero.");
            return;
        }

        // ✅ Si todo está correcto, actualizar el producto
        GestionProductos actualizado = new GestionProductos(
            id, nombre, entregado, recibido,
            descripcion, marca, cantidad, categoria, precio
        );

        dao.actualizarProducto(actualizado);
        mostrarProductos();
        limpiarCampos45();

        JOptionPane.showMessageDialog(this, "Producto actualizado correctamente ✏️");

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser números válidos.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al actualizar el producto: " + e.getMessage());
    }
} else {
    JOptionPane.showMessageDialog(this, "Selecciona un producto para modificar.");
}







    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
                                          
     int fila = tablaProductos.getSelectedRow();
    if (fila >= 0) {
        String id = tablaProductos.getValueAt(fila, 0).toString();
        dao.eliminarProducto(id);
        mostrarProductos();
        JOptionPane.showMessageDialog(this, "Producto eliminado 🗑️");
          limpiarCampos45();
    } else {
        JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
    }



    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

   cargarProductos();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
   int fila =  TablaProductos.getSelectedRow();
   if (fila != -1) {
        String nombre = TablaProductos.getValueAt(fila, 0).toString();
        String descripcion = TablaProductos.getValueAt(fila, 1).toString();
        String marca = TablaProductos.getValueAt(fila, 2).toString();
        String categoria = TablaProductos.getValueAt(fila, 3).toString();
        double precio = Double.parseDouble(TablaProductos.getValueAt(fila, 4).toString());
        int cantidad = (int)spinnerCantidad.getValue();
        
        

        CarritoTemp item = new CarritoTemp(nombre, descripcion, marca, categoria, precio, cantidad);
        carritoDAO.agregarProductoCarrito(item);

        JOptionPane.showMessageDialog(this, "Producto agregado al carrito");
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione un producto");
    }

    }//GEN-LAST:event_jButton14ActionPerformed

    private void TelefonoTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TelefonoTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TelefonoTxtActionPerformed

    private void btnSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseClicked
        new InicioSesion().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSalirMouseClicked

    private void txtNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
  int fila = TablaProductos.getSelectedRow();
    if (fila != -1) {
    String nombre =TablaProductos.getValueAt(fila, 0).toString();
    carritoDAO.eliminarProductoCarrito(nombre);
    JOptionPane.showMessageDialog(this, "Producto eliminado del carrito");
    mostrarCarrito(); // Vuelve a cargar
    }

    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        CompraDAO compraDAO = new CompraDAO();
        List<CarritoTemp> carrito = carritoDAO.listarcarrito();
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío. Agrega productos antes de realizar la compra.");
            return;
        }

        String nombre = textNombre.getText();
        String apellido = textApellidos.getText();
        String tipoD = (String) ComboTD.getSelectedItem();
        String numeroDoc = txtNumero.getText();
        String metodoPago = (String) ComboMetodoPago.getSelectedItem();
        String fecha = txtFecha.getText();
        String direccion = txtDireccion.getText();

//  Validaciones  
        if (nombre.isEmpty() || apellido.isEmpty() || numeroDoc.isEmpty() || fecha.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos obligatorios.");
            return;
        }

// Validación: Nombre solo letras
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(null, "El nombre solo debe contener letras.");
            return;
        }

// Validación: Apellido solo letras
        if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(null, "El apellido solo debe contener letras.");
            return;
        }

// Validación: Documento solo números
        if (!numeroDoc.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "El número de documento solo debe contener números.");
            return;
        }

        double subtotal = 0;
        for (CarritoTemp p : carrito) {
            subtotal += p.getPrecioProducto() * p.getCantidad();
        }

        double total = 0.0;
        for (int i = 0; i < TablaProductos.getRowCount(); i++) {
            double precio = Double.parseDouble(TablaProductos.getValueAt(i, 4).toString());
            int cantidad = Integer.parseInt(TablaProductos.getValueAt(i, 5).toString());
            total += precio * cantidad;
        }

        if ("Tarjeta".equalsIgnoreCase(metodoPago)) {
            String numeroTarjeta = JOptionPane.showInputDialog(this, "Ingrese el número de tarjeta:");
            if (numeroTarjeta == null || numeroTarjeta.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pago cancelado.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Pago realizado exitosamente con tarjeta terminada en "
                    + numeroTarjeta.substring(Math.max(0, numeroTarjeta.length() - 4)) + ".");

        }

        Compra compra = new Compra(nombre, apellido, tipoD, numeroDoc, metodoPago, carrito, direccion, fecha, subtotal, total);
        compraDAO.guardarCompra(compra);
        compraActual = compra;

        carritoDAO.vaciarCarrito();
        JOptionPane.showMessageDialog(this, "Compra realizada con éxito.\nTotal pagado: $" + compra.getTotal());
        limpiarCampos2();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void BuscarUsuarioCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarUsuarioCedulaActionPerformed

        // Ventana emergente para ingresar número de documento
        String numeroDoc = JOptionPane.showInputDialog(this, "Ingrese el número de documento del usuario:");

        if (numeroDoc == null || numeroDoc.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se ingresó ningún número de documento.");
            return;
        }

        // Buscar usuario
        Usuario usuario = usuarioDAO.buscarPorDocumento(numeroDoc.trim());

        if (usuario != null) {
            // Guardar la cédula original
            numeroDocumentoOriginal = usuario.getNumeroDocumento();

            // Llenar campos de texto
            N.setText(usuario.getNombres());
            A.setText(usuario.getApellidos());
            CE.setText(usuario.getCorreo());
            NC.setText(usuario.getTelefono());
            ND.setText(usuario.getNumeroDocumento());
            C.setText(usuario.getContraseña());

            // Llenar combos
            TD.setSelectedItem(usuario.getTipoDocumento());
            TU.setSelectedItem(usuario.getRol().name()); // Convertimos enum a String
        } else {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado");
        }


    }//GEN-LAST:event_BuscarUsuarioCedulaActionPerformed

    private void ModificarPerfilUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarPerfilUsuarioActionPerformed
                                               
                                                        
    if (numeroDocumentoOriginal == null || numeroDocumentoOriginal.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Primero busque el usuario que desea modificar.");
        return;
    }

    // Obtener valores de los campos
    String nombres = N.getText().trim();
    String apellidos = A.getText().trim();
    String correo = CE.getText().trim();
    String telefono = NC.getText().trim();
    String numeroDocumentoNuevo = ND.getText().trim();
    String tipoDocumento = (String) TD.getSelectedItem();
    String rolStr = (String) TU.getSelectedItem();
    String contrasena = C.getText().trim();

    // Validaciones
    if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || telefono.isEmpty() ||
        numeroDocumentoNuevo.isEmpty() || tipoDocumento.isEmpty() || rolStr.isEmpty() || contrasena.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
        return;
    }

    // Convertir rol de String a enum
    RolUsuarioEnum rol;
    try {
        rol = RolUsuarioEnum.valueOf(rolStr);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, "Rol de usuario inválido.");
        return;
    }

    // Cargar usuarios existentes
    List<Usuario> usuarios = usuarioDAO.listarUsuarios();
    boolean encontrado = false;

    // Buscar usuario por número original y actualizar
    for (Usuario u : usuarios) {
        if (u.getNumeroDocumento().equals(numeroDocumentoOriginal)) {
            u.setNombres(nombres);
            u.setApellidos(apellidos);
            u.setCorreo(correo);
            u.setTelefono(telefono);
            u.setTipoDocumento(tipoDocumento);
            u.setRol(rol);
            u.setContraseña(contrasena);
            u.setNumeroDocumento(numeroDocumentoNuevo); // Actualiza cédula también
            encontrado = true;
            break;
        }
    }

    if (encontrado) {
        usuarioDAO.guardarUsuarios(usuarios);
        JOptionPane.showMessageDialog(this, "Usuario modificado correctamente.");
        
        // Actualizar la cédula original para futuras modificaciones
        numeroDocumentoOriginal = numeroDocumentoNuevo;
         limpiarCampos5();
    } else {
        JOptionPane.showMessageDialog(this, "Usuario no encontrado. Busque primero el usuario.");
    }






    }//GEN-LAST:event_ModificarPerfilUsuarioActionPerformed

    private void btnBuscarCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCedulaActionPerformed
                                                 
     String cedula = JOptionPane.showInputDialog(this, "Ingresa la cédula a buscar:");
    if (cedula == null || cedula.trim().isEmpty()) return;

    cedula = cedula.trim();
    CompraDAO dao = new CompraDAO();
    List<Compra> compras = dao.cargarCompras1(); // todas las compras

    DefaultTableModel modelo = (DefaultTableModel) tablaHistorial.getModel();
    modelo.setRowCount(0); // limpia la tabla

    boolean encontrado = false;
    for (Compra c : compras) {
        if (c.getNumeroDocumento() != null && c.getNumeroDocumento().equals(cedula)) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNombreCliente() + " " + c.getApellidoCliente(),
                c.getNumeroDocumento(),
                c.getMetodoPago(),
                c.getFechaCompra(),
                c.getTotal(),
                "Ver",
                "PDF / Cancelar"
            });
            encontrado = true;
        }
    }

    if (!encontrado) {
        JOptionPane.showMessageDialog(this, "No se encontraron compras con esa cédula.");
        refrescarTabla(); // opcional: muestra todas de nuevo
    }
    



    }//GEN-LAST:event_btnBuscarCedulaActionPerformed

    private void ApellidoTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApellidoTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ApellidoTxtActionPerformed

    private void txtNumeroDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDocumentoActionPerformed

    private void ComboDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboDocumentoActionPerformed

    private void chkMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMostrarActionPerformed
        // TODO add your handling code here:
        if (chkMostrar.isSelected()){
            setContrasena.setEchoChar((char)0);
        }else{
            setContrasena.setEchoChar('*');
        }
    }//GEN-LAST:event_chkMostrarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         
  
        tablaProductos.clearSelection(); // quita la selección de la tabla
         limpiarCampos45();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaActionPerformed

    private void btnFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechaActionPerformed
        // TODO add your handling code here:
          mostrarCalendario();
    }//GEN-LAST:event_btnFechaActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
mostrarCarrito();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (compraActual == null) {
            JOptionPane.showMessageDialog(this, "No hay una compra reciente para generar la factura.");
            return;
        }

        GenerarFactura.generarFacturaPDF(compraActual);
        JOptionPane.showMessageDialog(this, "Factura generada correctamente.");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
       cargarTablaCompras();
    }//GEN-LAST:event_btnCargarActionPerformed

    private void NActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        limpiarCampos5();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void MActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MActionPerformed
        // TODO add your handling code here:
          if (M.isSelected()){
            C.setEchoChar((char)0);
        }else{
           C.setEchoChar('*');
        }
    }//GEN-LAST:event_MActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        new InicioSesion().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnBuscarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarUserActionPerformed
        filtrarUsuario();
    }//GEN-LAST:event_btnBuscarUserActionPerformed

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
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Administrador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField A;
    private javax.swing.JTabbedPane Administrador;
    private javax.swing.JTextField ApellidoTxt;
    private javax.swing.JButton BtnRegistrar;
    private javax.swing.JButton BuscarUsuarioCedula;
    private javax.swing.JPasswordField C;
    private javax.swing.JTextField CE;
    private javax.swing.JPanel CancelarCompra;
    private javax.swing.JComboBox<String> ComboDocumento;
    private javax.swing.JComboBox<String> ComboMetodoPago;
    private javax.swing.JComboBox<String> ComboRol;
    private javax.swing.JComboBox<String> ComboTD;
    private javax.swing.JPanel GestionProductos;
    private javax.swing.JPanel HistorialCompras;
    private javax.swing.JCheckBox M;
    private javax.swing.JButton ModificarPerfilUsuario;
    private javax.swing.JTextField N;
    private javax.swing.JTextField NC;
    private javax.swing.JTextField ND;
    private javax.swing.JTextField NombreTxt;
    private javax.swing.JPanel RealizarCompra;
    private javax.swing.JPanel Registro;
    private javax.swing.JComboBox<String> TD;
    private javax.swing.JComboBox<String> TU;
    private javax.swing.JTable TablaProductos;
    private javax.swing.JTextField TelefonoTxt;
    private javax.swing.JPanel Usuarios;
    private javax.swing.JButton btnBuscarCedula;
    private javax.swing.JButton btnBuscarUser;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JButton btnFecha;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JButton btnMostrarUsuarios;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JCheckBox chkMostrar;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPasswordField setContrasena;
    private javax.swing.JSpinner spinnerCantidad;
    private javax.swing.JTable tablaHistorial;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTable tablaUsuarios;
    private javax.swing.JTextField textApellidos;
    private javax.swing.JTextField textNombre;
    private javax.swing.JTextField txtBuscarDocumento;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCategoria;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEntregadoPor;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtNumeroDocumento;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtRecibidoPor;
    // End of variables declaration//GEN-END:variables

    private void limpiarCampos() {
        NombreTxt.setText("");
        ApellidoTxt.setText("");
        txtNumeroDocumento.setText("");
        txtCorreo.setText("");
        TelefonoTxt.setText("");
        setContrasena.setText("");
        ComboDocumento.setSelectedIndex(-1); // Deselecciona cualquier opción
        ComboRol.setSelectedIndex(-1);             // Deselecciona cualquier opción
    }

    
       
    private GestionProductos productoSeleccionado = null;

    private void limpiarCampos2() {
        textNombre.setText("");
        textApellidos.setText("");
        ComboTD.setSelectedIndex(-1);
         txtNumero.setText("");
           ComboMetodoPago.setSelectedIndex(-1);
            txtFecha.setText("");
         txtDireccion.setText("");
         
            

    }
    private void limpiarCampos45() {
        txtNombre.setText("");
        txtEntregadoPor.setText("");
        txtRecibidoPor.setText("");
        txtDescripcion.setText("");
        txtMarca.setText("");
        txtCantidad.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
    }
    private void limpiarCampos5() {
    // Limpiar campos de texto
    N.setText("");
    A.setText("");
    CE.setText("");
    NC.setText("");
    ND.setText("");
    C.setText(""); // Si es JPasswordField, también funciona
    
    // Limpiar combos y dejar opción por defecto
    TD.setSelectedIndex(0); // Asumiendo que el primer índice es "Seleccionar" o vacío
    TU.setSelectedIndex(0); // Lo mismo para rol de usuario
    
    // Limpiar la cédula original guardada
    numeroDocumentoOriginal = null;
}

}

