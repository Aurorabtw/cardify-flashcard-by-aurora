// src/create_quiz/QuizStorage.java
package create_quiz;

import java.io.*;
import java.util.*;

public class QuizStorage {
    private static final String QUIZ_FILE = "quizzes.dat";
    private static Map<String, List<QuizResult>> quizResults = new HashMap<>();

    static {
        loadQuizzes();
    }

    public static class QuizResult implements Serializable {
        private final String subject;
        private final Date date;
        private final int score;
        private final int totalQuestions;

        public QuizResult(String subject, int score, int totalQuestions) {
            this.subject = subject;
            this.date = new Date();
            this.score = score;
            this.totalQuestions = totalQuestions;
        }

        public String getSubject()           { return subject; }
        public Date   getDate()              { return date; }
        public int    getScore()             { return score; }
        public int    getTotalQuestions()    { return totalQuestions; }
        public double getPercentage()        { return (double) score / totalQuestions * 100; }
    }

    public static void saveQuizResult(String subject, int score, int totalQuestions) {
        QuizResult result = new QuizResult(subject, score, totalQuestions);
        quizResults.computeIfAbsent(subject, k -> new ArrayList<>()).add(result);
        saveQuizzes();
    }

    public static List<QuizResult> getQuizResults(String subject) {
        return new ArrayList<>(quizResults.getOrDefault(subject, Collections.emptyList()));
    }

    public static Map<String, List<QuizResult>> getAllQuizResults() {
        return new HashMap<>(quizResults);
    }

    private static void saveQuizzes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(QUIZ_FILE))) {
            oos.writeObject(quizResults);
        } catch (IOException e) {
            System.err.println("Error saving quiz results: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadQuizzes() {
        File file = new File(QUIZ_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                quizResults = (Map<String, List<QuizResult>>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading quiz results: " + e.getMessage());
        }
    }
}
