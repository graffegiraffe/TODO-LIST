package by.rublevskaya.todolist.log;

import by.rublevskaya.todolist.service.PropsHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLogger {
    private static final String INFO_LOG_FILE = getAbsoluteLogFilePath(PropsHandler.getPropertyFromConfig("INFO_LOG_FILE"));
    private static final String ERROR_LOG_FILE = getAbsoluteLogFilePath(PropsHandler.getPropertyFromConfig("ERROR_LOG_FILE"));

    private static String getAbsoluteLogFilePath(String logFilePath) {
        Path path = Paths.get(logFilePath);
        if (path.isAbsolute()) {
            return logFilePath;
        }
        String workingDir = System.getProperty("user.dir");
        return Paths.get(workingDir, logFilePath).toString();
    }

    private static void ensureLogFileExists(String logFilePath) {
        try {
            Path path = Paths.get(logFilePath);
            if (!Files.exists(path)) {
                System.out.println("[Debug] file file was not found: " + logFilePath);
                if (!Files.exists(path.getParent())) {
                    System.out.println("[Debug] I create a directory: " + path.getParent());
                    Files.createDirectories(path.getParent());
                }
                System.out.println("[Debug] Create a file: " + logFilePath);
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.err.printf("Error in creating a log file (%s): %s%n", logFilePath, e.getMessage());
            e.printStackTrace();
        }
    }

    public static void log(String level, String message, Throwable throwable, String logFile) {
        ensureLogFileExists(logFile);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = String.format("%s [%s] %s", timestamp, level.toUpperCase(), message);

        if (throwable != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            logMessage += "\n" + sw.toString();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(logMessage + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Log-file entry error" + logFile + ": " + e.getMessage());
        }
    }

    public static void info(String message) {
        log("INFO", message, null, INFO_LOG_FILE);
    }

    public static void error(String message) {
        log("ERROR", message, null, ERROR_LOG_FILE);
    }

    public static void error(String message, Throwable throwable) {
        log("ERROR", message, throwable, ERROR_LOG_FILE);
    }
}
