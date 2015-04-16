<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Query.Filter" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterPredicate" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
	
<h1>Hi Nora!</h1>

<h2>would you like to delete a lot of things?</h2>

<a href="manage.jsp?dict=eng">English</a>
<a href="manage.jsp?dict=span">Spanish</a>
<a href="manage.jsp?dict=trans">Translations</a>

<%
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    /* First, establish if the database has been populated */
    Query query;


    String num = request.getParameter("num");
    int numToDelete = 5000;
    if (num != null) {
        numToDelete = Integer.parseInt(num);
    } 

    String dict = request.getParameter("dict");
    if (dict != null) {
        if (dict.compareTo("span") == 0) {
            query = new Query("Spanish").addSort("lemma", Query.SortDirection.ASCENDING);
        } else if (dict.compareTo("trans") == 0) {
            query = new Query("Translation").addSort("english", Query.SortDirection.ASCENDING);
        } else {
            //eng
            query = new Query("English").addSort("lemma", Query.SortDirection.ASCENDING);
            dict = "eng";
        }
    } else {
        // eng
        query = new Query("English").addSort("lemma", Query.SortDirection.ASCENDING);
        dict = "eng";
    }
    pageContext.setAttribute("dict", dict);
	
    List<Entity> existingEntries = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
    
    if (!existingEntries.isEmpty()) {
        // Database has been populated, we can continue
        for (Entity e: existingEntries) {
            pageContext.setAttribute("thing", e.getProperty("lemma"));
            %>
            <p>${fn:escapeXml(thing)}</p>
            <%
        }
    } else {
        %>
            <p>It's empty!</p>
        <%
    }
    
    List<Entity> topEntries = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(numToDelete));
    for (Entity e: topEntries) {
        datastore.delete(e.getKey());
    }
    //datastore.delete(topEntries);
%>

<a href="manage.jsp?dict=${fn:escapeXml(dict)}">Delete 5000 entries!</a>
<a href="manage.jsp?dict=${fn:escapeXml(dict)}&num=10000">Delete 10,000 entries!</a>
        
</body>
</html>