package ru.af3412.dream.servlet;

import ru.af3412.dream.model.User;
import ru.af3412.dream.store.PsqlStore;
import ru.af3412.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class AuthServlet extends HttpServlet {

    private final static Store STORE = PsqlStore.instOf();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Optional<User> user = STORE.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                HttpSession sc = req.getSession();
                sc.setAttribute("user", user.get());
                resp.sendRedirect(req.getContextPath() + "/posts.do");
            } else {
                invalidAuthentication(req, resp);
            }
        } else {
            invalidAuthentication(req, resp);
        }

    }

    private void invalidAuthentication(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", "Не верный email или пароль");
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }
}
