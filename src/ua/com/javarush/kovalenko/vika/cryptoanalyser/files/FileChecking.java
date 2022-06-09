package ua.com.javarush.kovalenko.vika.cryptoanalyser.files;

import ua.com.javarush.kovalenko.vika.cryptoanalyser.exception.FileProcessingException;
import ua.com.javarush.kovalenko.vika.cryptoanalyser.model.Artifacts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class FileChecking {

    private static File file;
    private static String filePath;

    public FileChecking(Path userFilePath) {
        filePath = userFilePath.toString();
        file = new File(filePath);
        saveArtifact();
    }

    public static Path preliminaryPathCheck(Path filePath) {
        try {
            if (!filePath.toString().endsWith(".txt")) {
                throw new IllegalArgumentException("The program works only with .txt files. The program terminates...");
            }
        } catch (InvalidPathException ex) {
            throw new IllegalArgumentException("An invalid character was detected in the path! The program can no " +
                                               "longer run.");
        }
        return filePath;
    }

    public boolean isValidFilePath() {
        return Files.isRegularFile(Path.of(filePath));
    }

    private static void saveArtifact() {
        if (Artifacts.getInputFilePath() == null) {
            Artifacts.setInputFilePath(file.toPath());
        } else {
            if (!file.exists()) {
                try {
                    Files.createFile(file.toPath());
                } catch (IOException ex) {
                    throw new FileProcessingException("Failed to create file " + Path.of(filePath), ex);
                }
            }
            Artifacts.setOutputFilePath(file.toPath());
        }
    }
}
