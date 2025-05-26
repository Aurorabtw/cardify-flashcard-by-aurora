package create_flashcard;

import Utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class FlashcardPractice extends JFrame {
    private final JComboBox<String> subjectSelector;
    private final FlipCardPanel cardPanel;
    private List<Flashcard> cards;
    private int currentIndex = 0;
    private String currentSubject = "";

    public FlashcardPractice() {
        setTitle("Flashcard Practice");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        setContentPane(mainPanel);

        // 🔝 Title + subject selector
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        JLabel title = new JLabel("Practice Flashcards", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut(10));

        subjectSelector = new JComboBox<>();
        for (String subject : FlashcardStorage.getAllSubjects()) {
            subjectSelector.addItem(subject);
        }
        subjectSelector.setMaximumSize(new Dimension(300, 30));
        subjectSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        subjectSelector.setBackground(Color.WHITE);
        subjectSelector.setFont(UIUtils.FONT_GENERAL_UI);
        subjectSelector.addActionListener(e -> {
            currentSubject = (String) subjectSelector.getSelectedItem();
            loadCards();
        });

        topPanel.add(subjectSelector);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 📚 Card panel
        cardPanel = new FlipCardPanel();
        cardPanel.setPreferredSize(new Dimension(400, 200));
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // 🔁 Navigation buttons
        JButton prevButton = createButton("← Previous");
        JButton nextButton = createButton("Next →");

        prevButton.addActionListener(e -> showPreviousCard());
        nextButton.addActionListener(e -> showNextCard());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load initial cards
        if (subjectSelector.getItemCount() > 0) {
            currentSubject = (String) subjectSelector.getSelectedItem();
            loadCards();
        }

        setVisible(true);
    }

    public FlashcardPractice(String subject) {
        this();
        subjectSelector.setSelectedItem(subject);
    }

    private void loadCards() {
        cards = FlashcardStorage.getCards(currentSubject);
        currentIndex = 0;
        if (!cards.isEmpty()) {
            cardPanel.setCard(cards.get(currentIndex));
        } else {
            cardPanel.setCard(null);
        }
    }

    private void showNextCard() {
        if (cards == null || cards.isEmpty()) return;
        currentIndex = (currentIndex + 1) % cards.size();
        cardPanel.setCard(cards.get(currentIndex));
    }

    private void showPreviousCard() {
        if (cards == null || cards.isEmpty()) return;
        currentIndex = (currentIndex - 1 + cards.size()) % cards.size();
        cardPanel.setCard(cards.get(currentIndex));
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setFont(UIUtils.FONT_GENERAL_UI.deriveFont(Font.BOLD));
        btn.setBackground(new Color(80, 130, 190));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static class FlipCardPanel extends JPanel {
        private Flashcard card;
        private boolean showingFront = true;
        private int angle = 0;
        private Timer timer;

        public FlipCardPanel() {
            setBackground(UIUtils.COLOR_BACKGROUND);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    startFlip();
                }
            });
        }

        public void setCard(Flashcard card) {
            this.card = card;
            this.showingFront = true;
            this.angle = 0;
            repaint();
        }

        private void startFlip() {
            if (timer != null && timer.isRunning()) return;

            timer = new Timer(10, new ActionListener() {
                boolean flippingOut = true;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (flippingOut) {
                        angle += 10;
                        if (angle >= 90) {
                            showingFront = !showingFront;
                            flippingOut = false;
                        }
                    } else {
                        angle -= 10;
                        if (angle <= 0) {
                            angle = 0;
                            timer.stop();
                        }
                    }
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (card == null) {
                drawCenteredText(g, "No flashcards available", getWidth(), getHeight());
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(66, 66, 90));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));

            String text = showingFront ? card.getQuestion() : card.getAnswer();

            g2.translate(getWidth() / 2, getHeight() / 2);
            g2.rotate(Math.toRadians(angle));
            drawCenteredText(g2, text, 0, 0);
            g2.dispose();
        }

        private void drawCenteredText(Graphics g, String text, int centerX, int centerY) {
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            g.drawString(text, centerX - textWidth / 2, centerY + textHeight / 4);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlashcardPractice::new);
    }
}
