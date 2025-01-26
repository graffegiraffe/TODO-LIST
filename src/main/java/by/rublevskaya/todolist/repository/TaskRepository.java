package by.rublevskaya.todolist.repository;

import by.rublevskaya.todolist.log.CustomLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TaskRepository {
    private final Map<String, Set<String>> taskList;

    public TaskRepository() {
        taskList = new HashMap<>();
        CustomLogger.info("TaskRepository initialized.");
    }

    public Map<String, Set<String>> getTaskList() {
        CustomLogger.info("Retrieving the entire task list.");
        return taskList;
    }

    public Set<String> getTasksByUsername(String username) {
        CustomLogger.info("Fetching tasks for user: " + username);
        Set<String> tasks = taskList.getOrDefault(username, new HashSet<>());

        if (tasks.isEmpty()) {
            CustomLogger.info("No tasks found for user: " + username);
        } else {
            CustomLogger.info("Found " + tasks.size() + " task(s) for user: " + username);
        }
        return tasks;
    }

    public void addTask(String username, String task) {
        CustomLogger.info("Attempting to add a task for user: " + username);
        taskList.computeIfAbsent(username, k -> new HashSet<>()).add(task);
        CustomLogger.info("Task added for user: " + username + " | Task: " + task);
    }

    public void removeTask(String username, String task) {
        CustomLogger.info("Attempting to remove a task for user: " + username);
        Set<String> tasks = taskList.get(username);

        if (tasks != null && tasks.remove(task)) {
            CustomLogger.info("Task removed for user: " + username + " | Task: " + task);
        } else {
            CustomLogger.info("Task not found for user: " + username + " | Task: " + task);
        }
    }

    public void removeAllTasks(String username) {
        CustomLogger.info("Attempting to remove all tasks for user: " + username);

        if (taskList.remove(username) != null) {
            CustomLogger.info("All tasks removed for user: " + username);
        } else {
            CustomLogger.info("No tasks to remove for user: " + username);
        }
    }
}