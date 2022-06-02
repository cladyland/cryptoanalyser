package ua.com.javarush.kovalenko.vika.cryptoanalyser.model;

import java.nio.file.Path;

public class Artifacts {

    private static Path inputFilePath;
    private static Path outputFilePath;
    private static int cesarKey;

    private Artifacts() {
    }

    public static Path getInputFilePath() {
        return inputFilePath;
    }

    public static void setInputFilePath(Path inputFilePath) {
        Artifacts.inputFilePath = inputFilePath;
    }

    public static Path getOutputFilePath() {
        return outputFilePath;
    }

    public static void setOutputFilePath(Path outputFilePath) {
        Artifacts.outputFilePath = outputFilePath;
    }

    public static int getCesarKey() {
        return cesarKey;
    }

    public static void setCesarKey(int cesarKey) {
        Artifacts.cesarKey = cesarKey;
    }
}
