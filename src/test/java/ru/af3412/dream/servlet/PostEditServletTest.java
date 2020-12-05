package ru.af3412.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.af3412.dream.model.Post;
import ru.af3412.dream.store.PsqlStore;
import ru.af3412.dream.store.Store;
import ru.af3412.dream.store.StoreStub;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostEditServletTest {

    @Test
    public void whenGetPostWithoutIdThenReturnEmptyPost() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(req.getSession()).thenReturn(session);
        when(req.getParameter("id")).thenReturn(null);
        when(req.getRequestDispatcher("edit.jsp")).thenReturn(dispatcher);
        PostEditServlet postEditServlet = new PostEditServlet();
        postEditServlet.doGet(req, resp);
        assertThat(((Post) req.getAttribute("post")).getId(), is(0));
    }

    @Test
    public void whenAddPostThenStoreIt() throws ServletException, IOException {
        Store storeStub = new StoreStub();
        PowerMockito.mockStatic(PsqlStore.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(PsqlStore.instOf()).thenReturn(storeStub);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("Test");
        new PostEditServlet().doPost(req, resp);
        assertThat(storeStub.findAllPosts().iterator().next().getName(), is("Test"));
    }

}