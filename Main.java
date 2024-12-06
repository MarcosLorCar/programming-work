import java.io.File;
import java.util.Scanner;

public class Main {
    private static final Scanner KEYBOARD = new Scanner(System.in);
    private static String[][] easyWords;
    private static String[][] mediumWords;
    private static String[][] hardWords;
    private static String[] hangedManAscii;
    private static String difficulty = "none";


    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to the game of the hanged man!");

        // Read words in the files
        readFiles();

        menu();
    }

    static void menu() {
        boolean exit = false;
        do {
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
                    exit = true;
                    break;
                default:
                    System.out.println("Option selected is not available.");
            }
        } while (!exit);
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

        System.out.print(hangedManAscii[0]);

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
                        hidden[i] = shown[i];
                    }
                }
                boolean equals = true;

                for (int i=0; i<hidden.length; i++)
                    if (!(hidden[i] == shown[i]))
                        equals = false;

                // Check if word is completely shown
                if (equals) {
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
                System.out.print(hangedManAscii[wrongAnswer]);
                System.out.println("The chosen letter is wrong.");
                System.out.println("Mistakes: " + wrongAnswer + "/6");
                if (wrongAnswer >= 6) {
                    // Game over sequence
                    isPlaying = false;
                    System.out.println("You have reached 6 mistakes and the man has been hanged up. Game over.");

                    System.out.print("The word was: ");
                    for (int i=0; i<shown.length; i++) {
                        System.out.print(shown[i] + " ");
                    }
                    System.out.println();
                }
            }
        }
    }

    static void readFiles() throws Exception {
        // Initialize the words arrays
        File easyFile = new File("easy.txt");
        File mediumFile = new File("medium.txt");
        File hardFile = new File("hard.txt");

        easyWords = readWordPairs(easyFile);

        mediumWords = readWordPairs(mediumFile);

        hardWords = readWordPairs(hardFile);

        // Load the ASCII art as well
        File asciiFile = new File("ascii_arts.txt");
        Scanner asciiScanner = new Scanner(asciiFile);

        hangedManAscii = new String[7]; // Fixed size of 7 error arts

        int index = 0; // Keep track of where to store next complete art
        String figure = ""; // Variable to buffer each art
        while (asciiScanner.hasNextLine()) {
            // Read line by line
            String line = asciiScanner.nextLine();
            if (line != "") {
                figure += line + "\n";
            } else {
                // When there is a blank line, store the art accumulated and start again
                hangedManAscii[index] = figure;
                index++;
                figure = "";
            }

            // This is to store the last one, if there isn't a blank line at the end
            if ((!asciiScanner.hasNextLine()) && (line != ""))
                hangedManAscii[index] = figure;
        }
    }

    static int countLines(File file) throws Exception {
        int count = 0;
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            sc.nextLine();
            count++;
        }
        sc.close();
        return count;
    }

    static String[][] readWordPairs(File file) throws Exception {
        int i = 0;
        Scanner sc = new Scanner(file);
        int lineCount = countLines(file);

        // 2D array with the first index pointing to a pair (lineCount),
        // and the second meaning word or its category (2)
        String[][] words = new String[lineCount][2];
        while (sc.hasNextLine()) {
            words[i][0] = sc.nextLine();
            words[i][1] = sc.nextLine();
            i++;
        }
        sc.close();
        return words;
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