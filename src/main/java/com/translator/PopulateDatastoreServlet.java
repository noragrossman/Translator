package com.translator;

 /*
  * @author Nora Grossman
  */

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;


public class PopulateDatastoreServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

      	String mode = req.getParameter("mode");

      	// Add the task to the default queue.
        Queue queue = QueueFactory.getDefaultQueue();

        if (mode.compareTo("spanish") == 0) {
        	queue.add(TaskOptions.Builder.withUrl("/spanishWorker"));
        	System.out.println("spanish task has been added to queue");
        } else if (mode.compareTo("translation") == 0) {
        	queue.add(TaskOptions.Builder.withUrl("/transWorker"));
        	System.out.println("translations task has been added to queue");
        } else if (mode.compareTo("english") == 0) {
          queue.add(TaskOptions.Builder.withUrl("/englishWorker"));
          System.out.println("english task has been added to queue");
        } else if (mode.compareTo("spanishIrrConjs") == 0) {
          queue.add(TaskOptions.Builder.withUrl("/spanConjWorker"));
          System.out.println("spanish conjugations task has been added to queue");
        } else if (mode.compareTo("englishIrrConjs") == 0) {
          queue.add(TaskOptions.Builder.withUrl("/engConjWorker"));
          System.out.println("english conjugations task has been added to queue");
        } else {
        	System.out.println("bad mode");
        }
        

        resp.sendRedirect("/");

        
  }
}