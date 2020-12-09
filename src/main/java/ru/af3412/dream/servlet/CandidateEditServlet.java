package ru.af3412.dream.servlet;

import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.store.PsqlStore;
import ru.af3412.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@MultipartConfig
public class CandidateEditServlet extends HttpServlet {

    private final static Store STORE = PsqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Candidate candidate = getCandidateByRequest(req);
        req.setAttribute("candidate", candidate);
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.setAttribute("cities", STORE.findAllCities());
        req.getRequestDispatcher("edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Candidate candidate = STORE.saveCandidate(
                new Candidate(
                        Integer.parseInt(req.getParameter("id")),
                        req.getParameter("name"),
                        Integer.parseInt(req.getParameter("city")))
        );
        saveFileFromRequestForCandidate(req, candidate);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }

    private Candidate getCandidateByRequest(HttpServletRequest req) {
        if (req.getParameter("id") == null) {
            return new Candidate(0, "", 0, 0);
        }
        return STORE.findCandidateById(Integer.parseInt(req.getParameter("id")));
    }

    private void saveFileFromRequestForCandidate(HttpServletRequest req, Candidate candidate) throws IOException, ServletException {
        Part filePart = req.getPart("file");
        try (InputStream fileContent = filePart.getInputStream()) {
            File folder = new File("images");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(folder + File.separator + candidate.getPhotoId() + ".png");
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(fileContent.readAllBytes());
            }
        }

    }


}
