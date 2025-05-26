package create_quiz;

import create_flashcard.Flashcard;
import create_flashcard.FlashcardStorage;

public class SeedQuizData {
    public static void seed() {
        FlashcardStorage.addCard("Machine Learning", new Flashcard("What is overfitting?", "Fitting noise instead of pattern"));
        FlashcardStorage.addCard("C++", new Flashcard("What is a pointer?", "A variable that stores memory address"));
        FlashcardStorage.addCard("OOP", new Flashcard("What is polymorphism?", "Same interface, different implementation"));
        FlashcardStorage.addCard("Algorithms", new Flashcard("What is Big-O of binary search?", "O(log n)"));
        FlashcardStorage.addCard("Database", new Flashcard("What is normalization?", "Organizing data to reduce redundancy"));
        FlashcardStorage.addCard("English", new Flashcard("Synonym of happy?", "Joyful"));
    }
}
