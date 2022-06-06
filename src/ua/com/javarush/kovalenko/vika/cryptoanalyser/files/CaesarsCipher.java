package ua.com.javarush.kovalenko.vika.cryptoanalyser.files;

import ua.com.javarush.kovalenko.vika.cryptoanalyser.exception.FileProcessingException;
import ua.com.javarush.kovalenko.vika.cryptoanalyser.model.Alphabet;
import ua.com.javarush.kovalenko.vika.cryptoanalyser.model.Artifacts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CaesarsCipher {

    private static final int ALPHABET_LENGTH = Alphabet.getALPHABET().length;
    private static final List<String> INPUT_LINES = FileProcessing.getInputLines(Artifacts.getInputFilePath());
    private static final StringBuilder OUTPUT_LINES = new StringBuilder();
    private static final String ENCRYPT_POSITIVE = "encryptPositive";
    private static final String ENCRYPT_NEGATIVE = "encryptNegative";
    private static final String DECRYPT_POSITIVE = "decryptPositive";
    private static final String DECRYPT_NEGATIVE = "decryptNegative";
    private static final String MOST_COMMON_WORDS = "most_common_words.txt";
    private static int cipherKey = Artifacts.getCesarKey();
    private static int inputIndex;
    private static int outputIndex;

    private CaesarsCipher() {
    }

    public static void encryption() {
        if (cipherKey > 0) {
            caesarEncryptDecrypt(ENCRYPT_POSITIVE);
        } else {
            caesarEncryptDecrypt(ENCRYPT_NEGATIVE);
        }
    }

    public static void decryption() {
        if (cipherKey > 0) {
            caesarEncryptDecrypt(DECRYPT_POSITIVE);
        } else {
            caesarEncryptDecrypt(DECRYPT_NEGATIVE);
        }
    }

    public static void bruteForce() {
        char[] symbols;
        int numberOfLinesRequiredForVerification = 9;
        if (INPUT_LINES.size() > numberOfLinesRequiredForVerification) {
            symbols = String.valueOf(INPUT_LINES.subList(0, 9)).toCharArray();
        } else {
            symbols = String.valueOf(INPUT_LINES).toCharArray();
        }

        int maxKey = Alphabet.getALPHABET().length - 1;
        int probablyKey = 0;

        for (int i = maxKey; i >= -maxKey; i--) {
            var tempDecrypt = new StringBuilder();
            int matchingWordsCount = 0;
            if (i == 0) {
                continue;
            }
            for (char symbol : symbols) {
                if (symbol == '[' || symbol == ']') {
                    continue;
                }
                cipherKey = i;
                inputIndex = Arrays.binarySearch(Alphabet.getALPHABET(), symbol);
                if (i > 0) {
                    outputIndex = findOutputIndex(DECRYPT_POSITIVE, inputIndex);
                } else {
                    outputIndex = findOutputIndex(DECRYPT_NEGATIVE, inputIndex);
                }
                if (outputIndex < 0) {
                    break;
                }
                tempDecrypt.append(Alphabet.getALPHABET()[outputIndex]);
            }

            String[] tempDecryptedWords = String.valueOf(tempDecrypt).split(" ");
            for (String word : tempDecryptedWords) {
                if (mostCommonWords().contains(word)) {
                    matchingWordsCount++;
                }
            }
            if (matchingWordsCount > 0) {
                probablyKey = i;
                break;
            }
        }

        if (probablyKey != 0) {
            Artifacts.setCesarKey(probablyKey);
            decryption();
        } else {
            throw new FileProcessingException("Couldn't find key to text from file: " + Artifacts.getInputFilePath());
        }
    }

    public static void statisticalAnalysis() {
        Optional<Map.Entry<Character, Integer>> maxEntry = symbolStatistics()
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        char probablySpace = maxEntry.orElseThrow().getKey();
        cipherKey = Arrays.binarySearch(Alphabet.getALPHABET(), probablySpace);
        decryption();
    }

    private static void caesarEncryptDecrypt(String argument) {
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
                outputIndex = findOutputIndex(argument, inputIndex);

                OUTPUT_LINES.append(Alphabet.getALPHABET()[outputIndex]);
            }
            OUTPUT_LINES.append("\n");
        }
        FileProcessing.writeLines(Artifacts.getOutputFilePath(), OUTPUT_LINES.toString());
    }

    private static int findOutputIndex(String argument, int inputIndex) {
        int outputIndex;
        switch (argument) {
            case ENCRYPT_POSITIVE, DECRYPT_NEGATIVE -> {
                outputIndex = inputIndex + Math.abs(cipherKey);
                if (outputIndex > ALPHABET_LENGTH - 1) {
                    outputIndex -= ALPHABET_LENGTH;
                }
            }
            case ENCRYPT_NEGATIVE, DECRYPT_POSITIVE -> {
                outputIndex = inputIndex - Math.abs(cipherKey);
                if (outputIndex < 0) {
                    outputIndex += ALPHABET_LENGTH;
                }
            }
            default -> outputIndex = 0;
        }
        return outputIndex;
    }

    private static HashSet<String> mostCommonWords() {
        var hundredCommonWords = new HashSet<String>();
        try (var reader = new BufferedReader(new FileReader(MOST_COMMON_WORDS))) {
            int hundredWords = 100;
            for (int i = 0; i < hundredWords; i++) {
                hundredCommonWords.add(reader.readLine());
            }
        } catch (IOException ex) {
            throw new FileProcessingException("Error reading " + MOST_COMMON_WORDS + " file", ex);
        }
        return hundredCommonWords;
    }

    private static HashMap<Character, Integer> symbolStatistics() {
        var symbolStatistics = new HashMap<Character, Integer>();
        try (var reader = new BufferedReader(new FileReader(String.valueOf(Artifacts.getInputFilePath())))) {
            char[] buffer = new char[1000];
            int symbolsAmount = reader.read(buffer);
            for (int i = 0; i < symbolsAmount; i++) {
                Character key = buffer[i];
                int value;
                if (symbolStatistics.containsKey(key)) {
                    value = symbolStatistics.get(key) + 1;
                    symbolStatistics.replace(key, value);
                } else {
                    value = 1;
                    symbolStatistics.put(key, value);
                }
            }
        } catch (IOException ex) {
            throw new FileProcessingException("Error reading file: " + Artifacts.getInputFilePath(), ex);
        }
        return symbolStatistics;
    }
}
