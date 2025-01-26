package by.rublevskaya.todolist.servlet;

import by.rublevskaya.todolist.log.CustomLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import by.rublevskaya.todolist.repository.TaskRepository;

import java.io.IOException;
import java.util.Set;

@WebServlet("/todo")
public class TodoListServlet extends HttpServlet {
    private final TaskRepository taskRepository;

    public TodoListServlet() {
        this.taskRepository = new TaskRepository();
        CustomLogger.info("TodoListServlet initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = (String) req.getSession().getAttribute("username");

        if (username == null) {
            CustomLogger.info("Unauthorized access attempt to Todo list. Redirecting to login.");
            resp.sendRedirect("/login");
            return;
        }

        CustomLogger.info("Fetching tasks for user: " + username);
        Set<String> tasks = taskRepository.getTasksByUsername(username);

        req.setAttribute("username", username);
        req.setAttribute("tasks", tasks);
        req.getRequestDispatcher("/page/todo-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        String deletedTask = req.getParameter("deletedTask");
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            CustomLogger.info("Unauthorized task action attempt. Redirecting to login.");
            resp.sendRedirect("/login");
            return;
        }
        Set<String> tasks = taskRepository.getTasksByUsername(username);

        if (task != null) {
            tasks.add(task);
            taskRepository.getTaskList().put(username, tasks);
            CustomLogger.info("Task added for user: " + username + " | Task: " + task);
        }

        if (deletedTask != null) {
            tasks.remove(deletedTask);
            taskRepository.getTaskList().put(username, tasks);
            CustomLogger.info("Task deleted for user: " + username + " | Task: " + deletedTask);
        }

        req.setAttribute("tasks", tasks);
        req.getRequestDispatcher("/page/todo-list.jsp").forward(req, resp);
    }
}