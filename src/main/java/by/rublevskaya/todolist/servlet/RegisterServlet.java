package by.rublevskaya.todolist.servlet;

import by.rublevskaya.todolist.log.CustomLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import by.rublevskaya.todolist.repository.UserRepository;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        CustomLogger.info("Registration attempt for user: " + username);
        boolean userExists = UserRepository.getUsers().containsKey(username);

        if (userExists) {
            CustomLogger.info("Registration failed: user already exists: " + username);
            req.setAttribute("errorMessage", "User already exists.");
            req.getRequestDispatcher("/register.html").forward(req, resp);
        } else {
            UserRepository.addUser(username, password);
            CustomLogger.info("User registered successfully: " + username);
            HttpSession session = req.getSession();
            session.setAttribute("username", username);

            resp.sendRedirect("/todo");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomLogger.info("Registration page accessed.");
        req.getRequestDispatcher("/register.html").forward(req, resp);
    }
}