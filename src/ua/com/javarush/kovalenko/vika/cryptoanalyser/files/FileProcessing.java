package ua.com.javarush.kovalenko.vika.cryptoanalyser.files;

import ua.com.javarush.kovalenko.vika.cryptoanalyser.exception.FileProcessingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileProcessing {

    private FileProcessing() {
    }

    public static List<String> getInputLines(Path inputFilePath) {
        try {
            return Files.readAllLines(inputFilePath);
        } catch (IOException ex) {
            throw new FileProcessingException("Error reading from file: " + inputFilePath, ex);
        }
    }

    public static void writeLines(Path outputFilePath, String outputLine) {
        try {
            Files.writeString(outputFilePath, outputLine);
        } catch (IOException ex) {
            throw new FileProcessingException("Error writing to file: " + outputFilePath, ex);
        }
    }
}
