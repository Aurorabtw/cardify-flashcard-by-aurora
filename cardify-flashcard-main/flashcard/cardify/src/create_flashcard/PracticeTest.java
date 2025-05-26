package create_flashcard;

import Utils.UIUtils;
import component.Toaster;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class PracticeTest extends JFrame {
    private final JPanel panel;
    private final Toaster toaster;

    public PracticeTest() {
        setTitle("Flashcard Practice");
        setSize(800, 500);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(null);

        panel = new JPanel(null);
        panel.setBounds(0, 0, 800, 500);
        panel.setBackground(UIUtils.COLOR_BACKGROUND);
        add(panel);

        toaster = new Toaster(panel);
        addWindowControls();
        addTitle();
        addSubjectButtons();
        addBackButton();

        setVisible(true);
    }

    private void addWindowControls() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlPanel.setOpaque(false);
        controlPanel.setBounds(0, 0, 800, 40);

        controlPanel.add(createWindowButton("—", new Color(100, 100, 100), () -> setState(Frame.ICONIFIED)));
        controlPanel.add(createWindowButton("×", new Color(200, 70, 70), this::dispose));

        panel.add(controlPanel);
    }

    private JButton createWindowButton(String text, Color bgColor, Runnable action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(getModel().isPressed() ? bgColor.darker() :
                        getModel().isRollover() ? bgColor.brighter() : bgColor);
                g2.fillOval(0, 0, getWidth(), getHeight());

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, y);
            }
        };

        button.setPreferredSize(new Dimension(30, 30));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.addActionListener(_ -> action.run());

        return button;
    }

    private void addTitle() {
        JLabel title = new JLabel("Select a Subject to Practice", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 50, 800, 40);
        panel.add(title);
    }

    private void addSubjectButtons() {
        Set<String> subjects = FlashcardStorage.getAllSubjects();
        if (subjects.isEmpty()) {
            JLabel noSubjects = new JLabel("No flashcards available. Add cards first.", SwingConstants.CENTER);
            noSubjects.setFont(UIUtils.FONT_GENERAL_UI);
            noSubjects.setForeground(Color.WHITE);
            noSubjects.setBounds(0, 200, 800, 30);
            panel.add(noSubjects);
            return;
        }

        JPanel subjectPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        subjectPanel.setOpaque(false);

        for (String subject : subjects) {
            JButton button = createSubjectButton(subject);
            subjectPanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(subjectPanel);
        scrollPane.setBounds(150, 120, 500, 250);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
    }

    private JButton createSubjectButton(String subject) {
        JButton button = new JButton(subject) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(UIUtils.COLOR_INTERACTIVE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

                g2.setColor(Color.WHITE);
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                FontMetrics metrics = g2.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(subject)) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(subject, x, y);
            }
        };

        button.setPreferredSize(new Dimension(200, 50));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.addActionListener(_ -> {
            if (!FlashcardStorage.hasEnough(subject, 1)) {
                toaster.warn("This subject has no flashcards.");
                return;
            }
            dispose();
            new FlashcardPractice(subject).setVisible(true);
        });

        return button;
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back to Dashboard") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(new Color(70, 130, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

                g2.setColor(Color.WHITE);
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                FontMetrics metrics = g2.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth("Back to Dashboard")) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString("Back to Dashboard", x, y);
            }
        };

        backButton.setBounds(300, 400, 200, 44);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(_ -> {
            dispose();
            new dashboard.Dashboard().setVisible(true);
        });

        panel.add(backButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PracticeTest::new);
    }
}

