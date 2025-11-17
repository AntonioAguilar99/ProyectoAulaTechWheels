/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package techwheels.Clases.Enumeraciones;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import techwheels.Clases.CarritoTemp;
import techwheels.Clases.Compra;
import techwheels.Clases.GestionProductos;

/**
 *
 * @author ASUS
 */
public class GenerarFactura {
    
     public static void generarFacturaPDF(Compra factura) {
        try {
            // --- LEER ARCHIVO JSON ---
            String contenido = System.getProperty("java.io.tmpdir") + "factura.pdf";

           
            // --- CREAR DOCUMENTO PDF ---
            Document documento = new Document();
            String nombreArchivo = System.getProperty("user.home") + "/Downloads/factura.pdf";

            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            // Título
            Paragraph titulo = new Paragraph("FACTURA DE COMPRA",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20));
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(new Paragraph("\n"));

            // Información del cliente
            documento.add(new Paragraph("Cliente: " + factura.getNombreCliente() + " " + factura.getApellidoCliente()));
            documento.add(new Paragraph("Documento: " + factura.getTipoDocumento() + " - " + factura.getNumeroDocumento()));
            documento.add(new Paragraph("Método de pago: " + factura.getMetodoPago()));
            documento.add(new Paragraph("Fecha: " + factura.getFechaCompra()));
            documento.add(new Paragraph("\n\n"));

            // Tabla de productos
            PdfPTable tabla = new PdfPTable(6);
            tabla.addCell("Producto");
            tabla.addCell("Descripción");
            tabla.addCell("Marca");
            tabla.addCell("Categoria");
            tabla.addCell("Precio");
            tabla.addCell("Cantidad");

            for (CarritoTemp p : factura.getProductos()) {
                tabla.addCell(p.getNombreProducto());
                tabla.addCell(p.getDescripcionProducto());
                tabla.addCell(p.getMarcaProducto());
                tabla.addCell(p.getCategoriaProducto());
                tabla.addCell(String.valueOf(p.getPrecioProducto()));
                tabla.addCell(String.valueOf(p.getCantidad()));
            }

            documento.add(tabla);

            documento.add(new Paragraph("\n"));

            // Totales
            documento.add(new Paragraph("Subtotal: " + factura.getSubtotal()));
            documento.add(new Paragraph("Total: " + factura.getTotal(),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));

            documento.close();
            try {
               Thread.sleep(300); // pequeño delay para evitar bloqueo del archivo

                 File archivo = new File(nombreArchivo);

                 if (archivo.exists()) {
                     if (Desktop.isDesktopSupported()) {
                         Desktop.getDesktop().open(archivo);
                     } else {
                         System.out.println("Desktop no soportado por este sistema.");
                     }
                 } else {
                     System.out.println("El archivo PDF no fue encontrado: " + nombreArchivo);
                 }
             } catch (Exception ex) {
                 ex.printStackTrace();
             }

             System.out.println("PDF generado correctamente: " + nombreArchivo);

         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}
