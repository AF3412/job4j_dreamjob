package ru.af3412.dream.servlet;

import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.store.PsqlStore;
import ru.af3412.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateEditServlet extends HttpServlet {

    private final static Store STORE = PsqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Candidate candidate;
        if (req.getParameter("id") == null) {
            candidate = new Candidate(0, "");
        } else {
            candidate = STORE.findCandidateById(Integer.parseInt(req.getParameter("id")));
        }
        req.setAttribute("candidate", candidate);
        req.getRequestDispatcher("edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        STORE.save(
                new Candidate(
                        Integer.parseInt(req.getParameter("id")),
                        req.getParameter("name")
                )
        );
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
