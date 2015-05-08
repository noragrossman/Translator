<!-- Author Nora Grossman, 2015 -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Query.CompositeFilterOperator" %>
<%@ page import="com.google.appengine.api.datastore.Query.Filter" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterPredicate" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>

	<h1 class="title">Exploring Machine Translation</h1>

	<div class="navigation">
        <ul>
            <li><a href="dictionary.jsp">Dictionary</a></li>
            <li><a href="conjugator.jsp">Conjugator</a></li>
            <li><a href="translator.jsp">Translator</a></li>
            <li><a href="about.jsp">About</a></li>
        </ul>
    </div>
	
	<div class="main">
		<%
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		String word = request.getParameter("word");
		String pos = request.getParameter("pos");
		String mode = request.getParameter("mode");
		String translation = request.getParameter("trans");

		boolean isSpanishMode = false;
		if (mode != null) {
			if (mode.compareTo("spanish") == 0) {
				isSpanishMode = true;
			}
		}

		if (word != null && pos != null) {
			Filter lemmaFilter = new FilterPredicate("lemma", FilterOperator.EQUAL, word);
			Filter posFilter = new FilterPredicate("partOfSpeech", FilterOperator.EQUAL, pos);
			Filter wordFilter = CompositeFilterOperator.and(lemmaFilter, posFilter);
			Query query;
			if (isSpanishMode) {
				query = new Query("English").setFilter(wordFilter);
			} else {
				query = new Query("Spanish").setFilter(wordFilter);
			}

			Entity wordInfo = datastore.prepare(query).asSingleEntity();

			if (wordInfo != null) {
				String lemma = (String)wordInfo.getProperty("lemma");
				String gender = (String)wordInfo.getProperty("gender");
				String posAbbr = (String)wordInfo.getProperty("partOfSpeech");
				boolean isVerb = false;
				String partOfSpeech;
				if (posAbbr.compareTo("n") == 0) {
					partOfSpeech = "noun";
				} else if (posAbbr.compareTo("v") == 0) {
					partOfSpeech = "verb";
					isVerb = true;
				} else if (posAbbr.compareTo("adj") == 0) {
					partOfSpeech = "adjective";
				} else if (posAbbr.compareTo("adv") == 0) {
					partOfSpeech = "adverb";
				} else if (posAbbr.compareTo("conj") == 0) {
					partOfSpeech = "conjugation";
				} else {
					partOfSpeech = "unknown";
				}
				
				%>
				<h2><%=lemma%></h2>
				<h4><%=partOfSpeech%></h4>
				<%
					if (gender != null) {
						if (gender.compareTo("m") == 0) {
							%>
							(masculine)<br>
							<%
						} else {
							%>
							(feminine)<br>
							<%
						}
					}

					if (isVerb && !isSpanishMode) {
						%>
						<p>Hey, you found a verb! Would you like to <a href="conjugator.jsp?verb=<%=lemma%>">conjugate</a> it?</p>
						<%
					}

					
			} else {
				%>
				<p>Something went wrong, sorry :(</p>
				<%
			}

			%>
				<form action="/dictionary" method="post">
					<input type="hidden" name="mode" value=<%=mode%> />
					<input type="hidden" name="w" value=<%=translation%> />
					<input type="submit" value="Back to results..." class="button"/>
				</form>
			<%

		} else {
			%>
			<p>No parameters specified :(</p>
			<%
		}
	%>
	</div>
    
</body>

</html>