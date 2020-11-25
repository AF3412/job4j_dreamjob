package ru.af3412.dream.servlet;

import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.store.PsqlStore;
import ru.af3412.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CandidateServlet extends HttpServlet {

    private final static Store STORE = PsqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Candidate> allCandidates = STORE.findAllCandidates();
        req.setAttribute("candidates", allCandidates);
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }
}