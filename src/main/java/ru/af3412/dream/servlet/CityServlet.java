package ru.af3412.dream.servlet;

import com.google.gson.Gson;
import ru.af3412.dream.model.City;
import ru.af3412.dream.store.PsqlStore;
import ru.af3412.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CityServlet extends HttpServlet {

    private final static Store STORE = PsqlStore.instOf();
    private final static Gson GSON = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var cities = STORE.findAllCities();
        req.setAttribute("cities", cities);
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("cities.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String reqBody;
        try (InputStream inputStream = req.getInputStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            reqBody = reader.lines().collect(Collectors.joining());
        }
        City newCity = GSON.fromJson(reqBody, City.class);
        City city = STORE.addCity(newCity);
        String cityJson = GSON.toJson(city);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(cityJson);
        out.flush();
    }
}
