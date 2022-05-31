package ua.com.javarush.vika_kovalenko.cryptoanalyser;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

public class Dialog {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String GREETINGS = "Hello!\nThis program can encrypt and decrypt text files (*.txt) " +
                                            "using Caesar cipher. To continue, please select the program mode:";
    private static final String FAREWELL = "The program has successfully completed. Please, check the file: %s" +
                                           "\nGoodbye!";
    private static final String PROGRAM_MODES = """
            1 -> encryption with a cipher key
            2 -> decryption with a cipher key
            3 -> decryption using brute force
            4 -> decryption using statistical text analysis
            exit -> end the program""";
    private static final String WRONG_MODE = "What do you want to do? Enter '1', '2', '3', '4' or 'exit'.";
    private static final String WRONG_FILE_PATH = "Oh, no! You entered the wrong file path. The program will be " +
                                                  "completed.";

    private Dialog() {
    }

    public static void dialogWithUser() {
        int mode;
        System.out.println(GREETINGS + "\n" + PROGRAM_MODES);
        while (true) {
            if (SCANNER.hasNextInt()) {
                int userMode = SCANNER.nextInt();
                if (userMode < 1 || userMode > 4) {
                    System.err.println(WRONG_MODE);
                } else {
                    mode = userMode;
                    SCANNER.nextLine();
                    break;
                }
            } else {
                if (SCANNER.nextLine().equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                } else {
                    System.err.println(WRONG_MODE);
                }
            }
        }

        System.out.println("Enter the path to the file you want to process:");
        isFile(preliminaryPathCheck());

        System.out.println("Enter the file path in which you want to save the result:");
        isFile(preliminaryPathCheck());

        if (mode <= 2) {
            System.out.println("Enter the cipher key:");
            int maxKeyValue = Alphabet.getALPHABET().length - 1;
            while (true) {
                if (SCANNER.hasNextInt()) {
                    int key = SCANNER.nextInt();
                    if (key < -maxKeyValue || key > maxKeyValue) {
                        System.err.println("Incorrect key. Enter a number from -" + maxKeyValue + " to " + maxKeyValue);
                    } else {
                        Artifacts.setCesarKey(key);
                        break;
                    }
                } else {
                    System.err.println("Invalid key value. Please enter a numeric value:");
                    SCANNER.nextLine();
                }
            }
        }
        System.out.println("The process has begun...\n");
        programMode(mode);
        System.out.printf(FAREWELL, Artifacts.getOutputFilePath());
    }

    private static Path preliminaryPathCheck() {
        Path filePath;
        try {
            filePath = Path.of(SCANNER.nextLine());
            if (!filePath.toString().endsWith(".txt")){
                throw new IllegalArgumentException("The program works only with .txt files. The program terminates...");
            }
        } catch (InvalidPathException ex) {
            throw new IllegalArgumentException("An invalid character was detected in the path! The program can no " +
                                               "longer run.");
        }
        return filePath;
    }

    private static void isFile(Path filePath) {
        var fileChecking = new FileChecking(filePath);
        if (!fileChecking.isValidFilePath()) {
            System.err.println(WRONG_FILE_PATH);
            System.exit(1);
        }
    }

    private static void programMode(int modeNumber) {
        switch (modeNumber) {
            case 1 -> CaesarsCipher.encryption();
            case 2 -> CaesarsCipher.decryption();
            case 3 -> CaesarsCipher.bruteForce();
            case 4 -> CaesarsCipher.statisticalAnalysis();
        }
    }
}
