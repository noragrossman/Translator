package com.translator;

 /*
  * @author Nora Grossman
  */

import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

public class DictionaryServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

        String word = request.getParameter("w");

        // isEnglishMode is true when translating from English to Spanish
        boolean isEnglishMode = true;

        String mode = request.getParameter("mode");
        if (mode != null) {
          if ("spanish".compareTo(mode) == 0) {
            isEnglishMode = false;
          }
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Query the datastore for all translations matching the given word
        Filter propertyFilter;
        if (isEnglishMode) {
          propertyFilter = new FilterPredicate("english", FilterOperator.EQUAL, word);
        } else {
          propertyFilter = new FilterPredicate("spanish", FilterOperator.EQUAL, word);
        }
        Query q = new Query("Translation").setFilter(propertyFilter).addSort("partOfSpeech", Query.SortDirection.ASCENDING);

        List<Entity> translations = new ArrayList<Entity>();
        try {
          translations = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(6));
        } catch (DatastoreNeedIndexException dnie) {
          request.setAttribute("error", dnie.getMessage());
        }
        
    
        if (!translations.isEmpty()) {
          request.setAttribute("translations", translations);
          System.out.println("Found some stuff for " + word);
        } else {
          System.out.println("Didn't find anything for " + word);
        }

        request.setAttribute("lemma", word);
        request.getRequestDispatcher("dictionary.jsp").forward(request, response);
    }
}