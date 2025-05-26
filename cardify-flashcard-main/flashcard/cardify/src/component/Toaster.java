package component;

import javax.swing.*;
import java.awt.*;

public class Toaster {
    private final JPanel mainPanel;
    private JComponent currentToast;

    public Toaster(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void success(String message) {
        show(message, new Color(72, 199, 142)); // Green
    }

    public void error(String message) {
        show(message, new Color(220, 70, 70)); // Red
    }

    public void warn(String message) {
        show(message, new Color(235, 185, 60)); // Yellow
    }

    public void info(String message) {
        show(message, new Color(88, 144, 255)); // Blue
    }

    private void show(String message, Color background) {
        if (currentToast != null && currentToast.isShowing()) {
            mainPanel.remove(currentToast);
            mainPanel.revalidate();
            mainPanel.repaint();
        }

        ToasterBody toast = new ToasterBody(message, background);
        int width = 240;
        int height = 40;

        int x = (mainPanel.getWidth() - width) / 2;
        int startY = mainPanel.getHeight();         // start below the window
        int targetY = mainPanel.getHeight() - 80;   // target bottom padding

        toast.setBounds(x, startY, width, height);
        mainPanel.add(toast, 0);
        mainPanel.revalidate();
        mainPanel.repaint();
        currentToast = toast;

        // Slide-in animation
        Timer slideTimer = new Timer(10, null);
        slideTimer.addActionListener(e -> {
            Point pos = toast.getLocation();
            if (pos.y > targetY) {
                toast.setLocation(x, pos.y - 5);
                toast.repaint();
            } else {
                toast.setLocation(x, targetY);
                slideTimer.stop();

                // Start auto-dismiss after 1.5s
                Timer dismissTimer = new Timer(1500, ev -> {
                    mainPanel.remove(toast);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    currentToast = null;
                });
                dismissTimer.setRepeats(false);
                dismissTimer.start();
            }
        });
        slideTimer.start();
    }
}
