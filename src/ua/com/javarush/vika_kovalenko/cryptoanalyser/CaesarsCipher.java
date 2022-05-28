package ua.com.javarush.vika_kovalenko.cryptoanalyser;

import java.util.Arrays;
import java.util.List;

public class CaesarsCipher {

    private static final int ALPHABET_LENGTH = Alphabet.getALPHABET().length;
    private static final List<String> INPUT_LINES = FileProcessing.getInputLines();
    private static final StringBuilder OUTPUT_LINES = new StringBuilder();
    private static final int CIPHER_KEY = Artifacts.getCesarKey();

    private CaesarsCipher() {
    }

    public static void encryption() {
        if (CIPHER_KEY > 0) {
            caesarEncryptDecrypt("encryptPositive");
        } else {
            caesarEncryptDecrypt("encryptNegative");
        }
    }

    public static void decryption() {
        if (CIPHER_KEY > 0) {
            caesarEncryptDecrypt("decryptPositive");
        } else {
            caesarEncryptDecrypt("decryptNegative");
        }
    }

    private static void caesarEncryptDecrypt(String argument) {
        int inputIndex;
        int outputIndex;

        for (String line : INPUT_LINES) {
            char[] temp = line.toCharArray();

            for (char letter : temp) {
                inputIndex = Arrays.binarySearch(Alphabet.getALPHABET(), letter);
                if (inputIndex < 0) {
                    if (argument.startsWith("encrypt")) {
                        continue;
                    } else {
                        throw new FileProcessingException("Symbol " + letter + " not found. Unable to decrypt text " +
                                                          "from file: " + Artifacts.getInputFilePath());
                    }
                }

                switch (argument) {
                    case "encryptPositive", "decryptNegative" -> {
                        outputIndex = inputIndex + Math.abs(CIPHER_KEY);
                        if (outputIndex > ALPHABET_LENGTH - 1) {
                            outputIndex -= ALPHABET_LENGTH;
                        }
                    }
                    case "encryptNegative", "decryptPositive" -> {
                        outputIndex = inputIndex - Math.abs(CIPHER_KEY);
                        if (outputIndex < 0) {
                            outputIndex += ALPHABET_LENGTH;
                        }
                    }
                    default -> outputIndex = 0;
                }
                OUTPUT_LINES.append(Alphabet.getALPHABET()[outputIndex]);
            }
            OUTPUT_LINES.append("\n");
        }
        FileProcessing.writeLines(OUTPUT_LINES.toString());
    }
}
