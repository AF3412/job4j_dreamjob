package ru.af3412.dream.servlet;

import ru.af3412.dream.model.Post;
import ru.af3412.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostEditServlet extends HttpServlet {

    private final static Store STORE = Store.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Post post;
        if (req.getParameter("id") == null) {
            post = new Post(0, "");
        } else {
            post = STORE.findPostById(Integer.parseInt(req.getParameter("id")));
        }
        req.setAttribute("post", post);
        req.getRequestDispatcher("edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        STORE.save(
                new Post(
                        Integer.parseInt(req.getParameter("id")),
                        req.getParameter("name")
                )
        );
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }

}
