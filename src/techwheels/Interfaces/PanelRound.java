/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package techwheels.Interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author ASUS
 */
public class PanelRound extends JPanel {

   private int cornerRadius;

    public PanelRound(int radius) {
        this.cornerRadius = radius;
        setOpaque(false); // Importante para que se vea el fondo redondeado
    }

    @Override
    protected void paintComponent(Graphics g) {//Dibujar panel con bordes redondeados
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo blanco o el color de fondo del panel
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcs.width, arcs.height);

        g2.dispose();
    }
}
