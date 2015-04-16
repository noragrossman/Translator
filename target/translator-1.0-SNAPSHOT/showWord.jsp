<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Query.CompositeFilterOperator" %>
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
	<a href="alternateTranslateHome.jsp">Home</a>
	<h1>Welcome!</h1>

<%
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	String word = request.getParameter("word");
	String pos = request.getParameter("pos");

	if (word != null && pos != null) {
		Filter lemmaFilter = new FilterPredicate("lemma", FilterOperator.EQUAL, word);
		Filter posFilter = new FilterPredicate("partOfSpeech", FilterOperator.EQUAL, pos);
		Filter wordFilter = CompositeFilterOperator.and(lemmaFilter, posFilter);
		Query query = new Query("Spanish").setFilter(lemmaFilter);

		Entity wordInfo = datastore.prepare(query).asSingleEntity();

		if (wordInfo != null) {
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
			pageContext.setAttribute("lemma", wordInfo.getProperty("lemma"));
			pageContext.setAttribute("pos", partOfSpeech);
			pageContext.setAttribute("gender", gender);
			%>
			<h3>${fn:escapeXml(lemma)}</h3>
			<h5>${fn:escapeXml(pos)}</h5>
			<%
				if (gender != null) {
					if (gender.compareTo("m") == 0) {
						%>
						(masculine)
						<%
					} else {
						%>
						(feminine)
						<%
					}
				}
		} else {
			%>
			<p>Something went wrong, sorry :(</p>
			<%
		}
	} else {
		%>
		No parameters specified :(
		<%
	}
%>
</body>

</html>