package by.rublevskaya.todolist.servlet;

import by.rublevskaya.todolist.log.CustomLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/about-me")
public class AboutMeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomLogger.info("Request received for About Me page.");
        try {
            req.getRequestDispatcher("/page/about-me.html").forward(req, resp);
            CustomLogger.info("About Me page displayed successfully.");
        } catch (Exception e) {
            CustomLogger.error("Failed to display About Me page.", e);
            throw e;
        }
    }
}
