package login;

import javax.swing.*;
import java.awt.*;
import dashboard.Dashboard;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI() {
        setTitle("Cardify Login");
        setSize(420, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel bgPanel = new JPanel(new GridBagLayout());
        bgPanel.setBackground(new Color(241, 248, 242)); // Soft green background
        getContentPane().add(bgPanel);

        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(340, 340));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 195), 2, true),
                BorderFactory.createEmptyBorder(30, 25, 30, 25)
        ));
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));

        // Logo
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon logo = new ImageIcon(getClass().getResource("/login/logoo.png"));
            Image img = logo.getImage().getScaledInstance(130, 40, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logoLabel.setText("CARDIFY");
            logoLabel.setFont(new Font("Serif", Font.BOLD, 32));
            logoLabel.setForeground(new Color(95, 190, 120));
        }
        cardPanel.add(logoLabel);

        cardPanel.add(Box.createVerticalStrut(25));

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(60, 90, 75));
        cardPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 195), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        cardPanel.add(usernameField);

        cardPanel.add(Box.createVerticalStrut(12));

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(60, 90, 75));
        cardPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 195), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        cardPanel.add(passwordField);

        cardPanel.add(Box.createVerticalStrut(25));

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setBackground(new Color(95, 190, 120));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (isValidLogin(username, password)) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    Dashboard dashboard = new Dashboard();
                    dashboard.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        cardPanel.add(loginButton);
        bgPanel.add(cardPanel);
    }

    private boolean isValidLogin(String username, String password) {
        // Dummy logic
        return username.equals("admin") && password.equals("password");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
