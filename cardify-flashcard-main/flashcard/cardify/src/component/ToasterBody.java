package component;

import javax.swing.*;
import java.awt.*;

public class ToasterBody extends JComponent {
    private final String message;
    private final Color background;
    private final int width;
    private final int height;
    private JButton closeButton;

    public ToasterBody(String message, Color background) {
        this(message, background, 240, 40);
    }

    public ToasterBody(String message, Color background, int width, int height) {
        this.message = message;
        this.background = background;
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
        setLayout(null); // Use manual layout for button

        initCloseButton();
    }

    private void initCloseButton() {
        closeButton = new JButton("x"); // plain text, simple and clean
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        closeButton.setBounds(width - 28, 8, 18, 18); // tighter fit
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setBorder(null);
        closeButton.setForeground(Color.WHITE);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Optional hover effect
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setForeground(new Color(255, 120, 120)); // light red
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setForeground(Color.WHITE);
            }
        });

        closeButton.addActionListener(e -> {
            Container parent = getParent();
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
        });

        add(closeButton);
    }



    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(background);
        g2.fillRoundRect(0, 0, width, height, 15, 15);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int textX = 10;
        int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(message, textX, textY);

        g2.dispose();
    }
}
