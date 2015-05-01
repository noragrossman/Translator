package com.translator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

public class TranslationWorker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CSVParser csvParser = new CSVParser();
        int num = csvParser.parseTranslationsCsv();
        System.out.println("Translations: " + num);

    }
}