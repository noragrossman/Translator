package com.translator;

 /*
  *	@author Nora Grossman
  */

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

public class SpanishWorker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CSVParser csvParser = new CSVParser();
        int num = csvParser.parseSpanishCsv();
        System.out.println("Spanish: " + num);
    }
}