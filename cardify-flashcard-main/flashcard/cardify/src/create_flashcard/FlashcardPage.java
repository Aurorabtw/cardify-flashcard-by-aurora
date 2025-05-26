package create_flashcard;

import Utils.UIUtils;
import component.Toaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FlashcardPage extends JFrame {
    private final String subject;
    private final JPanel flashcardPanel;
    private final Toaster toaster;
    private final JTextField questionField;
    private final JTextField answerField;

    public FlashcardPage(String subject) {
        this.subject = subject;
        setTitle("Flashcards: " + subject);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(false); // ✅ Enable standard window controls
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(null);

        JLabel titleLabel = new JLabel("Flashcards: " + subject, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 20, getWidth(), 30);
        add(titleLabel);

        // Toaster
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        mainPanel.setBounds(0, 0, getWidth(), getHeight());
        toaster = new Toaster(mainPanel);
        add(mainPanel);

        // Input fields
        questionField = new JTextField();
        questionField.setBounds(100, 60, 400, 30);
        mainPanel.add(questionField);

        answerField = new JTextField();
        answerField.setBounds(100, 100, 400, 30);
        mainPanel.add(answerField);

        JButton addButton = new JButton("Add Flashcard");
        addButton.setBounds(200, 140, 180, 35);
        addButton.setBackground(new Color(122, 201, 160));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.addActionListener(this::addFlashcard);
        mainPanel.add(addButton);

        // Flashcard display grid
        flashcardPanel = new JPanel();
        flashcardPanel.setLayout(new GridLayout(0, 2, 10, 10));
        flashcardPanel.setBounds(100, 200, 400, 200);
        flashcardPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        mainPanel.add(flashcardPanel);

        JButton backButton = new JButton("< Back");
        backButton.setBounds(20, getHeight() - 70, 100, 30);
        backButton.setBackground(new Color(70, 120, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.addActionListener(e -> dispose());
        mainPanel.add(backButton);

        refreshFlashcards();
        setVisible(true);
    }

    private void addFlashcard(ActionEvent e) {
        String question = questionField.getText().trim();
        String answer = answerField.getText().trim();

        if (question.isEmpty() || answer.isEmpty()) {
            toaster.warn("Question and answer cannot be empty");
            return;
        }

        Flashcard card = new Flashcard(question, answer);
        FlashcardStorage.addCard(subject, card);
        toaster.success("Flashcard added!");

        questionField.setText("");
        answerField.setText("");
        refreshFlashcards();
    }

    private void refreshFlashcards() {
        flashcardPanel.removeAll();
        List<Flashcard> cards = FlashcardStorage.getCards(subject);

        for (int i = 0; i < cards.size(); i++) {
            JLabel cardLabel = new JLabel((i + 1) + "", SwingConstants.CENTER);
            cardLabel.setForeground(Color.WHITE);
            cardLabel.setOpaque(true);
            cardLabel.setBackground(new Color(40, 44, 60));
            cardLabel.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120)));
            cardLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            flashcardPanel.add(cardLabel);
        }

        flashcardPanel.revalidate();
        flashcardPanel.repaint();
    }
}
