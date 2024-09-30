import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class PuntoMedio extends JFrame {
    private JPanel panelDibujo;
    private JTextField[] campos;
    private String[] etiquetas = {"X1", "Y1", "X2", "Y2", "Radio", "Ancho", "Alto"};
    private int[] valores = new int[7];
    private Color[] colores = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};
    private int figuraSeleccionada = 0;

    public PuntoMedio() {
        setTitle("Algoritmo de Punto Medio");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de control
        JPanel panelControl = new JPanel(new GridBagLayout());
        panelControl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        campos = new JTextField[7];

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panelControl.add(new JLabel(etiquetas[i]), gbc);

            gbc.gridx = 1;
            campos[i] = new JTextField(10);
            campos[i].addKeyListener(new NumeroKeyListener());
            panelControl.add(campos[i], gbc);
        }

        JButton dibujar = new JButton("Dibujar");
        dibujar.addActionListener(e -> dibujarFigura());

        JButton limpiar = new JButton("Limpiar");
        limpiar.addActionListener(e -> limpiarDibujo());

        String[] figuras = {"Línea", "Cuadrado", "Rectángulo", "Círculo", "Óvalo"};
        JComboBox<String> comboFiguras = new JComboBox<>(figuras);
        comboFiguras.addActionListener(e -> {
            figuraSeleccionada = comboFiguras.getSelectedIndex();
            actualizarCampos();
        });

        gbc.gridx = 0;
        gbc.gridy = etiquetas.length;
        gbc.gridwidth = 2;
        panelControl.add(comboFiguras, gbc);

        gbc.gridy++;
        panelControl.add(dibujar, gbc);

        gbc.gridy++;
        panelControl.add(limpiar, gbc);

        mainPanel.add(panelControl, BorderLayout.WEST);

        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarFiguraSeleccionada(g);
            }
        };
        panelDibujo.setBackground(Color.WHITE);
        panelDibujo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(panelDibujo, BorderLayout.CENTER);

        add(mainPanel);

        actualizarCampos();
    }

    private void actualizarCampos() {
        for (JTextField campo : campos) {
            campo.setEnabled(false);
        }

        switch (figuraSeleccionada) {
            case 0: // Línea
                habilitarCampos(0, 1, 2, 3);
                break;
            case 1: // Cuadrado
                habilitarCampos(0, 1, 5);
                break;
            case 2: // Rectángulo
                habilitarCampos(0, 1, 5, 6);
                break;
            case 3: // Círculo
                habilitarCampos(0, 1, 4);
                break;
            case 4: // Óvalo
                habilitarCampos(0, 1, 5, 6);
                break;
        }
    }

    private void habilitarCampos(int... indices) {
        for (int index : indices) {
            campos[index].setEnabled(true);
        }
    }

    private void dibujarFigura() {
        for (int i = 0; i < campos.length; i++) {
            if (campos[i].isEnabled()) {
                try {
                    valores[i] = Integer.parseInt(campos[i].getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.");
                    return;
                }
            }
        }
        panelDibujo.repaint();
    }

    private void limpiarDibujo() {
        for (JTextField campo : campos) {
            campo.setText("");
        }

        for (int i = 0; i < valores.length; i++) {
            valores[i] = 0;
        }

        panelDibujo.repaint();
    }

    private void dibujarFiguraSeleccionada(Graphics g) {
        g.setColor(colores[figuraSeleccionada]);
        switch (figuraSeleccionada) {
            case 0:
                dibujarLinea(g, valores[0], valores[1], valores[2], valores[3]);
                break;
            case 1:
                dibujarCuadrado(g, valores[0], valores[1], valores[5]);
                break;
            case 2:
                dibujarRectangulo(g, valores[0], valores[1], valores[5], valores[6]);
                break;
            case 3:
                dibujarCirculo(g, valores[0], valores[1], valores[4]);
                break;
            case 4:
                dibujarOvalo(g, valores[0], valores[1], valores[5], valores[6]);
                break;
        }
    }

    private void putPixel(Graphics g, int x, int y) {
        g.drawLine(x, y, x, y);
    }

    private void dibujarLinea(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            putPixel(g, x1, y1);
            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    private void dibujarCuadrado(Graphics g, int x, int y, int lado) {
        dibujarRectangulo(g, x, y, lado, lado);
    }

    private void dibujarRectangulo(Graphics g, int x, int y, int ancho, int alto) {
        dibujarLinea(g, x, y, x + ancho, y);
        dibujarLinea(g, x + ancho, y, x + ancho, y + alto);
        dibujarLinea(g, x + ancho, y + alto, x, y + alto);
        dibujarLinea(g, x, y + alto, x, y);
    }

    private void dibujarCirculo(Graphics g, int x0, int y0, int radio) {
        int x = radio;
        int y = 0;
        int err = 0;

        while (x >= y) {
            putPixel(g, x0 + x, y0 + y);
            putPixel(g, x0 + y, y0 + x);
            putPixel(g, x0 - y, y0 + x);
            putPixel(g, x0 - x, y0 + y);
            putPixel(g, x0 - x, y0 - y);
            putPixel(g, x0 - y, y0 - x);
            putPixel(g, x0 + y, y0 - x);
            putPixel(g, x0 + x, y0 - y);

            y += 1;
            err += 1 + 2 * y;
            if (2 * (err - x) + 1 > 0) {
                x -= 1;
                err += 1 - 2 * x;
            }
        }
    }

    private void dibujarOvalo(Graphics g, int x, int y, int a, int b) {
        int a2 = a * a;
        int b2 = b * b;
        int fa2 = 4 * a2, fb2 = 4 * b2;
        int x1, y1, sigma;

        /* first half */
        for (x1 = 0, y1 = b, sigma = 2 * b2 + a2 * (1 - 2 * b); b2 * x1 <= a2 * y1; x1++) {
            putPixel(g, x + x1, y + y1);
            putPixel(g, x - x1, y + y1);
            putPixel(g, x + x1, y - y1);
            putPixel(g, x - x1, y - y1);
            if (sigma >= 0) {
                sigma += fa2 * (1 - y1);
                y1--;
            }
            sigma += b2 * ((4 * x1) + 6);
        }

        /* second half */
        for (x1 = a, y1 = 0, sigma = 2 * a2 + b2 * (1 - 2 * a); a2 * y1 <= b2 * x1; y1++) {
            putPixel(g, x + x1, y + y1);
            putPixel(g, x - x1, y + y1);
            putPixel(g, x + x1, y - y1);
            putPixel(g, x - x1, y - y1);
            if (sigma >= 0) {
                sigma += fb2 * (1 - x1);
                x1--;
            }
            sigma += a2 * ((4 * y1) + 6);
        }
    }

    private class NumeroKeyListener extends KeyAdapter {
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                e.consume();
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new PuntoMedio().setVisible(true));
    }
}