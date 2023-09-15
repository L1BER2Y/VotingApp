package by.itAcademy.JD2.application;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@WebServlet(urlPatterns = "/vote")
public class VotingServlet extends HttpServlet {
    private static final int MIN_PICKS = 3;
    private static final int MAX_PICKS = 5;
    private static final int NUMBER_ONE = 1;
    private static Map<String, Integer> SINGER_RESULTS = new HashMap<>();
    private static  Map<String, Integer> GENRES_RESULTS = new HashMap<>();
    private static  Map<String, String> ABOUT_RESULTS = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");

        String singer = req.getParameter("singer");
        String[] genres = req.getParameterValues("genre");
        String about = req.getParameter("about");
        String localDateTime = getLocalDateTime();

        isValid(genres);
        putInSingerMap(singer);
        putInGenresMap(genres);
        putInAboutMap(localDateTime, about);

        PrintWriter writer = resp.getWriter();
        writer.write("<p>" + "Список лучших исполнителей:" + "</p>" +
                "<p>" + sortByValue(SINGER_RESULTS) + "</p>" +
                "<p>" + "Список лучших жанров:" + "</p>" +
                "<p>" + sortByValue(GENRES_RESULTS) + "</p>" +
                "<p>" + "Краткий текст о себе:" + "</p>" +
                "<p>" + sortByKey(ABOUT_RESULTS) + "</p>"
        );
    }

    public static void isValid(String[] values) {
        if (values.length < MIN_PICKS || values.length > MAX_PICKS) {
            throw new IllegalArgumentException("\"Форма заполнена неверно!\"");
        }
    }

    public static String getLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return now.format(formatter);
    }

    public static void putInSingerMap(String key) {
        if (SINGER_RESULTS.containsKey(key)) {
            Integer value = SINGER_RESULTS.get(key);
            SINGER_RESULTS.put(key, Math.addExact(value, 1));
        } else {
            SINGER_RESULTS.putIfAbsent(key, NUMBER_ONE);
        }
    }

    public static void putInGenresMap(String[] keys) {
        for (String key : keys) {
            if (GENRES_RESULTS.containsKey(key)) {
                Integer value = GENRES_RESULTS.get(key);
                GENRES_RESULTS.put(key, Math.addExact(value, 1));
            } else {
                GENRES_RESULTS.putIfAbsent(key, NUMBER_ONE);
            }
        }
    }

    public static void putInAboutMap(String dateAndTime, String about) {
        ABOUT_RESULTS.putIfAbsent(dateAndTime, about);
    }

    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByKey());
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}








