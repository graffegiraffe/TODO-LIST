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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        CustomLogger.info("Login attempt for user: " + username);
        boolean isValidUser = UserRepository.isValid(username, password);

        if (isValidUser) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            CustomLogger.info("User logged in successfully: " + username);
            resp.sendRedirect("/todo");
        } else {
            CustomLogger.info("Login failed for user: " + username);
            req.getRequestDispatcher("/login.html").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = (String) req.getSession().getAttribute("username");
        if (username == null) {
            CustomLogger.info("User not logged in. Redirecting to login page.");
            req.getRequestDispatcher("/login.html").forward(req, resp);
        } else {
            CustomLogger.info("User already logged in: " + username);
            req.getRequestDispatcher("/todo").forward(req, resp);
        }
    }
}