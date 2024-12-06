import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final String[][] easyWords = new String[20][2];
    private static final String[][] mediumWords = new String[20][2];
    private static final String[][] hardWords = new String[20][2];
    private static String difficulty = "none";


    private static final Scanner KEYBOARD = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to the game of the hanged man!");

        // Read words in the files
        readFiles();

        boolean exit;

        do {
            exit = menu();
        } while (!exit);
    }

    static boolean menu() {
        int option;
        System.out.printf("""

            Select one of the following options:
            1 - Play.
            2 - Select difficulty. [%s]
            3 - Exit.

            Enter the option you wish to choose:
            -> \s""", difficulty);

        option = KEYBOARD.nextInt();

        switch (option) {
            // Entering the play option.
            case 1:
                if (difficulty == "none") {
                    System.out.println("First you have to select the difficulty.");
                } else {
                    play();
                }
                break;
            // Entering the difficulty option.
            case 2:
                difficultyMenu();
                break;
            // Exiting the program.
            case 3:
                return true;
            default:
                System.out.println("Option selected is not available.");
        }
        return false;
    }

    static String[] getRandomWord() {
        int randomNumber = (int) (Math.random()*20);
        String[] word = new String[2];

        switch (difficulty) {
            // Both the word and the category are selected.
            case "easy":
                word = easyWords[randomNumber];
                break;
            case "medium":
                word = mediumWords[randomNumber];
                break;
            case "hard":
                word = hardWords[randomNumber];
                break;
        }
        return word;
    }

    static void play() {
        int wrongAnswer = 0;

        // Chose random word
        String[] randomWord = getRandomWord();
        String word = randomWord[0];
        String category = randomWord[1];

        // Initialize hidden word, category and wrong answers count
        String[] shown = new String[word.length()];
        String[] hidden = new String[word.length()];

        for (int i=0; i<word.length(); i++) {
            shown[i] = String.valueOf(word.charAt(i));
            hidden[i] = "_";
        }

        boolean isPlaying = true;

        System.out.println("The word has "+ word.length() + " letters.");
        System.out.println("The word's category is \"" + category + "\"");

        // Game loop
        while(isPlaying) {
            System.out.println(); // For aesthetic purposes
            // Display hidden word
            System.out.print("The hidden word is as follows: ");
            for (int i=0;i<hidden.length;i++){
                System.out.print(hidden[i] + " ");
            }
            System.out.println();

            // Ask for valid* letter
            System.out.print("Choose a letter: ");
            String letter = KEYBOARD.next().toLowerCase();

            if (word.toLowerCase().contains(letter)) {
                for (int i = 0; i<shown.length; i++) {
                    if (shown[i].equalsIgnoreCase(letter)) {
                        hidden[i] = i == 0 ? shown[i].toUpperCase() : shown[i];
                    }
                }

                // Check if word is completely shown
                if (Arrays.equals(shown, hidden)) {
                    isPlaying = false;
                    System.out.println("Congratulations, you guessed the word!");

                // Showing the full word.
                    for (int i=0;i<shown.length;i++) {
                        System.out.print(shown[i] + " ");
                    }
                    System.out.println();
                } else {
                    System.out.println("The chosen letter is right!");
                }
            } else {
                // Wrong letter is chosen
                wrongAnswer++;
                System.out.println("Mistakes: " + wrongAnswer + "/6");
                if (wrongAnswer >= 6) {
                    isPlaying = false;
                    System.out.println("You have reached 6 mistakes and the man has been hanged up. Game over.");
                    for (int i=0;i<shown.length;i++) {
                        System.out.print(shown[i] + " ");
                    }
                }
            }
        }
    }

    static void readFiles() throws Exception {
        Scanner easySc = new Scanner(new File("easy.txt"));
        Scanner mediumSc = new Scanner(new File("medium.txt"));
        Scanner hardSc = new Scanner(new File("hard.txt"));

        for (int i = 0; i < easyWords.length; i++) {
            if(easySc.hasNext()){
                easyWords[i][0] = easySc.nextLine();
                easyWords[i][1] = easySc.nextLine();
            }
        }
        easySc.close();

        for (int i = 0; i < mediumWords.length; i++) {
            if(mediumSc.hasNext()){
                mediumWords[i][0] = mediumSc.nextLine();
                mediumWords[i][1] = mediumSc.nextLine();
            }
        }
        mediumSc.close();

        for (int i = 0; i < hardWords.length; i++) {
            if(hardSc.hasNext()){
                hardWords[i][0] = hardSc.nextLine();
                hardWords[i][1] = hardSc.nextLine();
            }
        }
        hardSc.close();
    }

    static void difficultyMenu() {
        int optionDifficulty;
        // Display 3 difficulties and loop if invalid option
        do {
            System.out.print("""
                    Choose difficulty:
                    1 - Easy.
                    2 - Medium.
                    3 - Hard.
                
                -> \s""");
            optionDifficulty = KEYBOARD.nextInt();

            if (optionDifficulty < 1 || optionDifficulty > 3) {
                System.out.println("Error: invalid option selected. Try again.");
            }
        } while (optionDifficulty < 1 || optionDifficulty > 3);

        switch (optionDifficulty) {
            case 1:
                difficulty = "easy";
                break;
            case 2:
                difficulty = "medium";
                break;
            case 3:
                difficulty = "hard";
                break;
        }
    }

}