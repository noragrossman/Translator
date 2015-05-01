package com.translator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.translator.partsOfSpeech.*;

public class TranslatorServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    if (req.getParameter("testing") != null) {
      resp.setContentType("text/plain");
      resp.getWriter().println("Hello, this is a testing servlet. \n\n");
      Properties p = System.getProperties();
      p.list(resp.getWriter());

    } else {
      String english = req.getParameter("eng");
      System.out.println("Received word " + english);

      /*if (eng != null) {
        Filter propertyFilter = new FilterPredicate("english", FilterOperator.EQUAL, eng);
        query = new Query("Translation").setFilter(propertyFilter).addSort("partOfSpeech", Query.SortDirection.ASCENDING);
      } else {
        query = new Query("Translation").addSort("english", Query.SortDirection.ASCENDING);
      }
      
      List<Entity> translations = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));

      if (!translations.isEmpty()) {
        for (Entity translation : translations) {
          String pos = translation.getProperty("partOfSpeech");
        }
      }
      */

      

      /**String english = req.getParameter("englishWord");



      Translator translator = new Translator();
      ArrayList<Word> translations = translator.translate(english);

      if (translations != null && translations.size() > 0) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key key = KeyFactory.createKey("Translation", english);

        if (!entityExists(datastore, key)) {
          System.out.println("Entity " + english + " with key " + key + " does not previously exist");
          List<Entity> translationEntities = new ArrayList<Entity>();

          // For each entry found...
          for (Word word : translations) {
            Entity translation = new Entity("Translation", key);
            translation.setProperty("english", word.getLemma());

            // Get Spanish translations
            ArrayList<String> spanishSyns = word.getTranslations();
            String spanishTranslation = "";
            for (String syn : spanishSyns) {
              spanishTranslation += syn + " ";
            }
            translation.setProperty("spanish", spanishTranslation);

            // Get part of speech
            String pos = "w";
            if (word instanceof Noun) {
              pos = "n";
              Noun noun = (Noun) word;
              if (noun.getIsMasc()) {
                translation.setProperty("gender", "masculine");
              } else {
                translation.setProperty("gender", "feminine");
              }
            } else if (word instanceof Verb) {
              pos = "v";
            } else if (word instanceof Adjective) {
              pos = "a";
            } else if (word instanceof Adverb) {
              pos = "adv";
            } else if (word instanceof Preposition) {
              pos = "prep";
            } else if (word instanceof Conjunction) {
              pos = "conj";
            } else if (word instanceof Pronoun) {
              pos = "pro";
            }
            translation.setProperty("pos", pos);

            // Add to list of Entities to be pushed to the Datastore
            translationEntities.add(translation);
          }

          // Push Entities into the Datastore
          System.out.println("Pushing to datastore with key " + key);
          datastore.put(translationEntities);
        } else {
          System.out.println("Entity " + english + " with key " + key + " already exists");
        }
      }

      resp.sendRedirect("/translateHome.jsp?english=" + english);
      */
    }
  }

  private static boolean entityExists(DatastoreService ds, Key key) {
    Transaction tx = ds.beginTransaction();
    boolean result;

    Query query = new Query("Translation", key);
    List<Entity> translations = ds.prepare(query).asList(FetchOptions.Builder.withLimit(5));
    if (!translations.isEmpty()) {
      result = true;
    } else {
      result = false;
    }

    tx.commit();
    return result;
  }
}