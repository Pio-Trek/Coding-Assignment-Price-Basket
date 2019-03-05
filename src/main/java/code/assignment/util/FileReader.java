package code.assignment.util;

import code.assignment.exception.PropertiesFileNotFound;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is responsible for reading a stock file and converting
 * it into a String variable.
 */
public final class FileReader {

    private FileReader() {
    }

    public static String get(String path) {
        String fileContent = null;
        Path filePath = Paths.get(path);

        if (filePath.toFile().exists()) {

            try {
                fileContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.out.println("[ Error occurred when reading a file: " + e.getMessage() + "]");
            }

        } else {
            throw new PropertiesFileNotFound("[ Unable to find stock properties file: " + path + " ]");
        }

        return fileContent;
    }

}
