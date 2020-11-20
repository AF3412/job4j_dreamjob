package ru.af3412.dream.servlet;

import ru.af3412.dream.store.PsqlStore;
import ru.af3412.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {

    private final static Store STORE = PsqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", STORE.findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

}