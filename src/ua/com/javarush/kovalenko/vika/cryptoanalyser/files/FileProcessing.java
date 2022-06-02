package ua.com.javarush.kovalenko.vika.cryptoanalyser.files;

import ua.com.javarush.kovalenko.vika.cryptoanalyser.exception.FileProcessingException;
import ua.com.javarush.kovalenko.vika.cryptoanalyser.model.Artifacts;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileProcessing {

    private FileProcessing() {
    }

    public static List<String> getInputLines() {
        try {
            return Files.readAllLines(Artifacts.getInputFilePath());
        } catch (IOException ex) {
            throw new FileProcessingException("Error reading from file: " + Artifacts.getInputFilePath(), ex);
        }
    }

    public static void writeLines(String outputLine) {
        try {
            Files.writeString(Artifacts.getOutputFilePath(), outputLine);
        } catch (IOException ex) {
            throw new FileProcessingException("Error writing to file: " + Artifacts.getOutputFilePath(), ex);
        }
    }
}
