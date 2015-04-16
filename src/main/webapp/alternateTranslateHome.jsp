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
	
<h1>Welcome!</h1>

<%
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	Query query;

	String eng = request.getParameter("eng");
   
   // Is a translation request being made?
	if (eng != null) {
		Filter propertyFilter = new FilterPredicate("english", FilterOperator.EQUAL, eng);
		query = new Query("Translation").setFilter(propertyFilter).addSort("partOfSpeech", Query.SortDirection.ASCENDING);

		List<Entity> translations = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));

		if (!translations.isEmpty()) {
			pageContext.setAttribute("english", eng);
		%>
		    <table>
		    	<tr><th>Part of Speech</th><th>English</th><th>Spanish</th></tr>
		    	<%
		        for (Entity translation : translations) {
		            pageContext.setAttribute("english", translation.getProperty("english"));
		            pageContext.setAttribute("spanish", translation.getProperty("spanish"));
		            pageContext.setAttribute("pos", translation.getProperty("partOfSpeech"));
		            String url = "showWord.jsp?word=" + translation.getProperty("spanish") + "&pos=" + translation.getProperty("partOfSpeech");
		            pageContext.setAttribute("url", url);
		            %>
		            <tr><td>${fn:escapeXml(pos)}</td>
		            	<td>${fn:escapeXml(english)}</td>
		            	<td><a href=${fn:escapeXml(url)}>${fn:escapeXml(spanish)}</a></td>
		            </tr>
		            <%
		        }
		        %>
			</table>

			<form action="/" method="post">
				<div>
					<h3>Enter an English word:</h3>
					<input type="text" name="eng"/>
				</div>
				<div>
					<input type="submit" value="Go"/>
				</div>
			</form>
		 <%
		
		} else if (!translations.isEmpty() && eng != null) {
			%>
				<p>The word was not found, sorry!</p>

				<form action="/" method="post">
					<div>
						<h3>Enter an English word:</h3>
						<input type="text" name="eng"/>
					</div>
					<div>
						<input type="submit" value="Go"/>
					</div>
				</form>
			<%
		} else {
			%>
			<form action="/populate" method="post">
				<div>
					<h3>Try it!</h3>
					<p>(This will take just a minute)</p>
				</div>
				<div><input type="submit" value="Go!"/></div>
			</form>
			<%
		}
	} else {
		query = new Query("Translation").addSort("english", Query.SortDirection.ASCENDING);

		List<Entity> translations = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));

        // Has dictionary been populated?
		if (!translations.isEmpty()) {
            // Yes
            for (Entity e: translations) {
                pageContext.setAttribute("thing", e.getProperty("english"));
                %>
                    <p>${fn:escapeXml(thing)}</p>
                <%
            }
			%>
			<p>Give it a try!</p>
			<form action="/" method="post">
				<div>
					<h3>Enter an English word:</h3>
					<input type="text" name="eng"/>
				</div>
				<div>
					<input type="submit" value="Go"/>
				</div>
			</form>
			<%
		} else {
            // No
			%>
			<form action="/populate" method="post">
				<div>
					<h3>Try it!</h3>
					<p>(This will take just a minute)</p>
				</div>
				<div><input type="submit" value="Go!"/></div>
			</form>
			<%
		}
	}
	

	String success = request.getParameter("success");
	if (success != null) {
		if (success.compareTo("yes") == 0) {
			%> <p>Success putting items into datastore!</p><%
		} else {
			%> <p>Oops, something went wrong. Check the log.</p><%
		}
	}
%>

</body>
</html>