package by.rublevskaya.todolist.repository;

import by.rublevskaya.todolist.log.CustomLogger;
import by.rublevskaya.todolist.service.PropsHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static final Map<String, String> users = new HashMap<>();
    private static final String FILE_PATH;

    static {
        FILE_PATH = PropsHandler.getPropertyFromConfig("USERS");
        CustomLogger.info("Initializing UserRepository. Loading users from file: " + FILE_PATH);
        loadUsersFromFile();
        CustomLogger.info("UserRepository initialized with users: " + users.keySet());
    }

    public static Boolean isValid(String username, String password) {
        if (username == null || password == null) {
            CustomLogger.info("Validation failed: username or password is null.");
            return false;
        }
        boolean isValid = users.containsKey(username) && users.get(username).equals(password);
        CustomLogger.info("Validation result for user '" + username + "': " + (isValid ? "SUCCESS" : "FAILURE"));
        return isValid;
    }

    public static synchronized void addUser(String username, String password) {
        if (users.containsKey(username)) {
            CustomLogger.info("Attempted to add user '" + username + "', but user already exists.");
        } else {
            users.put(username, password);
            saveUsersToFile();
            CustomLogger.info("New user added: " + username);
        }
    }

    public static Map<String, String> getUsers() {
        CustomLogger.info("Retrieving all users. Total: " + users.size());
        return users;
    }

    private static void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
            CustomLogger.info("Users successfully saved to file: " + FILE_PATH);
        } catch (IOException e) {
            CustomLogger.error("Failed to save users to file: " + FILE_PATH, e);
        }
    }

    private static void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            CustomLogger.info("No user file found at " + FILE_PATH + ". Skipping file load.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
            CustomLogger.info("Users successfully loaded from file: " + FILE_PATH);
        } catch (IOException e) {
            CustomLogger.error("Failed to load users from file: " + FILE_PATH, e);
        }
    }
}