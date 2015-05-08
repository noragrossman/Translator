package com.translator;

/*
* @author Nora Grossman
*/

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ConjugationServlet extends HttpServlet {
  private static final String CONJ_ERROR_MESSAGE = "Couldn't find one or more of the request conjugations :(";

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {

      	String verb = req.getParameter("verb");
      	String tense = req.getParameter("tense");

      	Conjugator c = new Conjugator();

        ArrayList<String> conjugations = new ArrayList<String>();

      	for (int i = 0; i < 6; i++) {
          String s = c.conjugateVerb(verb, Integer.parseInt(tense), i);
          if (s != null) {
            conjugations.add(s);
          } else {
            conjugations.add("x");
            req.setAttribute("error", CONJ_ERROR_MESSAGE);
          }
      	}

        req.setAttribute("infinitive", verb);
      	req.setAttribute("conjugations", conjugations);
		    req.getRequestDispatcher("conjugator.jsp").forward(req, resp);
  }
}