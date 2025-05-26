package utils;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

public class ThemeManager {

    public static void setNimbusTheme() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Customize Nimbus theme
        UIManager.put("Button.background", new Color(100, 160, 255)); // Button background color
        UIManager.put("Button.foreground", Color.WHITE); // Button text color
        UIManager.put("Panel.background", new Color(240, 240, 240)); // Panel background
        UIManager.put("control", new Color(240, 240, 240)); // Control background color
        UIManager.put("Label.foreground", new Color(50, 50, 50)); // Label text color
        UIManager.put("ComboBox.background", new Color(255, 255, 255)); // ComboBox background color
    }

    public static void setMetalTheme() {
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Customize Metal theme
        UIManager.put("Button.background", new Color(175, 238, 255)); // Light blue button background
        UIManager.put("Button.foreground", Color.BLACK); // Black button text
        UIManager.put("Panel.background", new Color(240, 240, 240)); // Panel background
        UIManager.put("control", new Color(240, 240, 240)); // Control background color
        UIManager.put("Label.foreground", new Color(0, 0, 0)); // Label text color
        UIManager.put("ComboBox.background", new Color(255, 255, 255)); // ComboBox background color
    }

    public static void setCustomTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Customize the system theme (customize colors as needed)
        UIManager.put("Button.background", new Color(100, 200, 100)); // Green button background
        UIManager.put("Button.foreground", Color.BLACK); // Button text color
        UIManager.put("Panel.background", new Color(245, 245, 245)); // Light gray panel background
        UIManager.put("control", new Color(245, 245, 245)); // Control background color
        UIManager.put("Label.foreground", new Color(0, 0, 0)); // Label text color
        UIManager.put("ComboBox.background", new Color(255, 255, 255)); // ComboBox background color
    }

    // Method to switch themes dynamically if needed
    public static void setTheme(String theme) {
        switch (theme.toLowerCase()) {
            case "nimbus":
                setNimbusTheme();
                break;
            case "metal":
                setMetalTheme();
                break;
            case "custom":
                setCustomTheme();
                break;
            default:
                setNimbusTheme();  // Default to Nimbus if no valid theme is provided
        }
    }
}
