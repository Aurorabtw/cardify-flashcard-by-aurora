package create_quiz;

import create_flashcard.Flashcard;
import create_flashcard.FlashcardStorage;
import component.Toaster;
import Utils.UIUtils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public class QuizPage extends JFrame {
    private final String subject;
    private final List<Flashcard> questions;
    private final JPanel panel;
    private final Toaster toaster;
    private Flashcard currentQuestion;
    private int currentIndex = 0;
    private int score = 0;

    public QuizPage(String subject) {
        this.subject = subject;
        this.questions = new ArrayList<>(FlashcardStorage.getCards(subject));
        Collections.shuffle(this.questions);

        setTitle("Quiz - " + subject);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Null layout so all setBounds calls work
        panel = new JPanel(null);
        panel.setBackground(UIUtils.COLOR_BACKGROUND);
        add(panel);

        toaster = new Toaster(panel);
        addWindowControls();

        if (questions.isEmpty()) {
            showNoCardsMessage();
        } else {
            loadNextQuestion();
        }
    }

    private void addWindowControls() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlPanel.setOpaque(false);
        controlPanel.setBounds(0, 0, getWidth(), 40);

        JButton minimizeButton = createControlButton("—", new Color(100, 100, 100));
        minimizeButton.addActionListener(e -> setState(Frame.ICONIFIED));

        JButton closeButton = createControlButton("×", new Color(200, 70, 70));
        closeButton.addActionListener(e -> dispose());

        controlPanel.add(minimizeButton);
        controlPanel.add(closeButton);
        panel.add(controlPanel);
    }

    private JButton createControlButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                Color fill = getModel().isPressed()   ? bgColor.darker()
                        : getModel().isRollover()  ? bgColor.brighter()
                        : bgColor;
                g2.setColor(fill);
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
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showNoCardsMessage() {
        panel.removeAll();

        JLabel message = new JLabel("No flashcards available for " + subject, SwingConstants.CENTER);
        message.setFont(UIUtils.FONT_GENERAL_UI);
        message.setForeground(Color.WHITE);
        message.setBounds(0, 150, 800, 40);
        panel.add(message);

        JButton back = createActionButton("Back to Dashboard", 250, () -> {
            dispose();
            new dashboard.Dashboard();
        });
        panel.add(back);
        panel.repaint();
    }

    private void loadNextQuestion() {
        panel.removeAll();
        addWindowControls();

        if (currentIndex >= questions.size()) {
            showQuizCompleted();
            return;
        }

        currentQuestion = questions.get(currentIndex);
        createQuestionUI();
        panel.repaint();
    }

    private void createQuestionUI() {
        JLabel qLabel = new JLabel(
                "<html><div style='text-align:center;'>Q" + (currentIndex + 1) + ": "
                        + currentQuestion.getQuestion()
                        + "</div></html>",
                SwingConstants.CENTER
        );
        qLabel.setFont(UIUtils.FONT_GENERAL_UI);
        qLabel.setForeground(Color.WHITE);
        qLabel.setBounds(50, 80, 700, 60);
        panel.add(qLabel);

        JLabel scoreLabel = new JLabel(
                "Score: " + score + "/" + questions.size(),
                SwingConstants.RIGHT
        );
        scoreLabel.setFont(UIUtils.FONT_GENERAL_UI);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(600, 20, 150, 30);
        panel.add(scoreLabel);

        int y = 160;
        for (String opt : generateOptions(currentQuestion.getAnswer())) {
            panel.add(createOptionButton(opt, y));
            y += 60;
        }
    }

    private JLabel createOptionButton(String text, int y) {
        JLabel option = new JLabel(
                "<html><div style='text-align:center;padding:10px;'>" + text + "</div></html>",
                SwingConstants.CENTER
        );
        option.setFont(UIUtils.FONT_GENERAL_UI);
        option.setOpaque(true);
        option.setBackground(new Color(66, 133, 244));
        option.setForeground(Color.WHITE);
        option.setBounds(250, y, 300, 50);
        option.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        option.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        option.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleAnswerSelection(text);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                option.setBackground(new Color(51, 103, 214));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                option.setBackground(new Color(66, 133, 244));
            }
        });

        return option;
    }

    private void handleAnswerSelection(String selected) {
        if (selected.equals(currentQuestion.getAnswer())) {
            score++;
            toaster.success("Correct!");
        } else {
            toaster.error("Wrong! Correct: " + currentQuestion.getAnswer());
        }
        currentIndex++;
        // javax.swing.Timer for UI delay
        Timer t = new Timer(1000, e -> loadNextQuestion());
        t.setRepeats(false);
        t.start();
    }

    private List<String> generateOptions(String correct) {
        Set<String> distractors = new HashSet<>();
        for (Flashcard fc : FlashcardStorage.getCards(subject)) {
            if (!fc.getAnswer().equals(correct) && distractors.size() < 3) {
                distractors.add(fc.getAnswer());
            }
        }
        List<String> opts = new ArrayList<>(distractors);
        opts.add(correct);
        while (opts.size() < 4) {
            opts.add("Sample Answer " + (opts.size() + 1));
        }
        Collections.shuffle(opts);
        return opts;
    }

    private void showQuizCompleted() {
        panel.removeAll();

        JLabel done = new JLabel(
                "<html><div style='text-align:center;'>Quiz Completed!<br>"
                        + "Final Score: " + score + "/" + questions.size()
                        + "</div></html>",
                SwingConstants.CENTER
        );
        done.setFont(new Font("Segoe UI", Font.BOLD, 24));
        done.setForeground(Color.WHITE);
        done.setBounds(0, 150, 800, 100);
        panel.add(done);

        panel.add(createActionButton("Restart Quiz", 250, () -> {
            currentIndex = 0;
            score = 0;
            Collections.shuffle(questions);
            loadNextQuestion();
        }));
        panel.add(createActionButton("Back to Dashboard", 350, () -> {
            dispose();
            new dashboard.Dashboard();
        }));

        panel.repaint();
    }

    private JButton createActionButton(String text, int y, Runnable action) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                g2.setColor(UIUtils.COLOR_INTERACTIVE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);
                g2.setColor(Color.WHITE);
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int yy = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, yy);
            }
        };
        btn.setBounds(300, y, 200, 44);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizPage("Sample Subject"));
    }
}
