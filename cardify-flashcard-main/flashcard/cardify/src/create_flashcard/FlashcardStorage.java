package create_flashcard;

import java.io.*;
import java.util.*;

public class FlashcardStorage {
    private static final String FILE_NAME = "flashcards.dat";
    private static Map<String, List<Flashcard>> flashcardStore = new HashMap<>();

    static {
        loadFlashcards();
    }

    public static void addCard(String subject, Flashcard card) {
        flashcardStore.putIfAbsent(subject, new ArrayList<>());
        flashcardStore.get(subject).add(card);
        saveFlashcards();
    }

    public static void addSubject(String subject) {
        flashcardStore.putIfAbsent(subject, new ArrayList<>());
        saveFlashcards();
    }

    public static void clearSubject(String subject) {
        flashcardStore.remove(subject); // ✅ completely removes subject
        saveFlashcards();
    }

    public static List<Flashcard> getCards(String subject) {
        return flashcardStore.getOrDefault(subject, new ArrayList<>());
    }

    public static boolean hasEnough(String subject, int min) {
        return getCards(subject).size() >= min;
    }

    public static Set<String> getAllSubjects() {
        return flashcardStore.keySet();
    }

    public static boolean subjectExists(String subject) {
        return flashcardStore.containsKey(subject);
    }

    public static void save() {
        saveFlashcards();
    }

    private static void saveFlashcards() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(flashcardStore);
        } catch (IOException e) {
            System.err.println("Error saving flashcards: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadFlashcards() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                flashcardStore = (Map<String, List<Flashcard>>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading flashcards: " + e.getMessage());
            }
        }
    }

    public static void clearAll() {
        flashcardStore.clear();
        saveFlashcards();
    }
}
