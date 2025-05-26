package dashboard;



import create_flashcard.FlashcardPractice;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import dashboard.WrapLayout;

public class Dashboard extends JFrame {
    private List<String> subjectList; // however you load/store subjects

    public Dashboard() {
        setTitle("Dashboard - Cardify");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(38, 50, 56));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(38, 50, 56));
        header.setLayout(new BorderLayout());
        JLabel title = new JLabel("Flashcards");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("Select a subject to begin");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(new Color(190, 200, 210));

        JPanel headerLabels = new JPanel();
        headerLabels.setLayout(new BoxLayout(headerLabels, BoxLayout.Y_AXIS));
        headerLabels.setBackground(new Color(38, 50, 56));
        headerLabels.add(Box.createVerticalStrut(18));
        headerLabels.add(title);
        headerLabels.add(subtitle);

        header.add(headerLabels, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Card panel
        JPanel cardPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 35, 20));
        cardPanel.setOpaque(false);

        // Example subject list; use your own source
        subjectList = List.of("Java", "Computer Science", "Python");
        for (String subject : subjectList) {
            JPanel subjectCard = new JPanel();
            subjectCard.setPreferredSize(new Dimension(220, 140));
            subjectCard.setLayout(new BoxLayout(subjectCard, BoxLayout.Y_AXIS));
            subjectCard.setBackground(getSubjectColor(subject));
            subjectCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

            JLabel subjectLabel = new JLabel(subject);
            subjectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            subjectLabel.setForeground(Color.WHITE);
            subjectLabel.setHorizontalAlignment(SwingConstants.CENTER);

            subjectCard.add(subjectLabel);
            subjectCard.add(Box.createVerticalStrut(18));

            JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonRow.setOpaque(false);

            JButton practiceBtn = new JButton("Practice");
            practiceBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            practiceBtn.setBackground(new Color(197, 80, 91));
            practiceBtn.setForeground(Color.WHITE);
            practiceBtn.setFocusPainted(false);
            practiceBtn.setPreferredSize(new Dimension(90, 34));
            // **This line is the fix**
            practiceBtn.addActionListener(e -> new FlashcardPractice(subject));

            JButton quizBtn = new JButton("QUIZ");
            quizBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            quizBtn.setBackground(new Color(55, 61, 61));
            quizBtn.setForeground(Color.WHITE);
            quizBtn.setFocusPainted(false);
            quizBtn.setPreferredSize(new Dimension(90, 34));
            // Add your quiz logic as before

            buttonRow.add(practiceBtn);
            buttonRow.add(quizBtn);

            subjectCard.add(buttonRow);
            cardPanel.add(subjectCard);
        }

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom bar (Practice All, Add)
        JPanel bottomBar = new JPanel();
        bottomBar.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 18));
        bottomBar.setBackground(new Color(38, 50, 56));

        JButton practiceAllBtn = new JButton("Practice All");
        practiceAllBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        practiceAllBtn.setBackground(new Color(120, 160, 230));
        practiceAllBtn.setForeground(Color.WHITE);
        practiceAllBtn.setFocusPainted(false);
        practiceAllBtn.setPreferredSize(new Dimension(140, 40));
        // Your existing logic for Practice All
        bottomBar.add(practiceAllBtn);

        JButton addSubjectBtn = new JButton("+");
        addSubjectBtn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        addSubjectBtn.setBackground(new Color(170, 222, 180));
        addSubjectBtn.setForeground(Color.WHITE);
        addSubjectBtn.setFocusPainted(false);
        addSubjectBtn.setPreferredSize(new Dimension(55, 40));
        // Your logic for adding subject
        bottomBar.add(addSubjectBtn);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private Color getSubjectColor(String subject) {
        switch (subject) {
            case "Java":
                return new Color(155, 220, 130);
            case "Computer Science":
                return new Color(210, 200, 200);
            case "Python":
                return new Color(205, 175, 175);
            default:
                return new Color(120, 160, 200);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
