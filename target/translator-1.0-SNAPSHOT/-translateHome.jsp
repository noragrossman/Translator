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
<a href="populate.jsp">Click here to go to management console</a>
<%
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    /* First, establish if the database has been populated */
	Query query = new Query("Translation").addSort("english", Query.SortDirection.ASCENDING);
    List<Entity> existingEntries = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
    
    //The following is just for the database population, will not appear otherwise
    String numPut = request.getParameter("num");
	if (numPut != null) {
        int n = Integer.parseInt(numPut);
		if (n != 0) {
			%> 
            <p>Success putting items into datastore! <%=n%> entities put.</p>
            <%
		} else {
			%> <p>Oops, something went wrong. Check the log.</p><%
		}
	}
    
    if (!existingEntries.isEmpty()) {
        // Database has been populated, we can continue
        for (Entity e: existingEntries) {
            //pageContext.setAttribute("thing", e.getProperty("english"));
            %>
            <p><%=e.getProperty("english")%></p>
            <%
        }
    
        String eng = request.getParameter("eng");
        if (eng != null) {
            // A request is being made
            Filter propertyFilter = new FilterPredicate("english", FilterOperator.EQUAL, eng);
            Query q = new Query("Translation").setFilter(propertyFilter).addSort("partOfSpeech", Query.SortDirection.ASCENDING);

            List<Entity> translations = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(10));
    
            if (!translations.isEmpty()) {
                // We have successfully translated a word! Let's show them.
                %>
                <table>
                    <tr><th>Part of Speech</th><th>English</th><th>Spanish</th></tr>
                    <%
                    for (Entity translation : translations) {
                        //pageContext.setAttribute("english", translation.getProperty("english"));  ${fn:escapeXml(pos)}
                        //pageContext.setAttribute("spanish", translation.getProperty("spanish"));
                        //pageContext.setAttribute("pos", translation.getProperty("partOfSpeech"));
                        String url = "showWord.jsp?word=" + translation.getProperty("spanish") + "&pos=" + translation.getProperty("partOfSpeech");
                        pageContext.setAttribute("url", url);
                        %>
                        <tr><td><%=translation.getProperty("partOfSpeech")%></td>
                            <td><%=translation.getProperty("english")%></td>
                            <td><a href=<%=url%>><%=translation.getProperty("spanish")%></a></td>
                        </tr>
                        <%
                    }
                    %>
                </table>
                <%
            } else {
                // We couldn't find the translation
                %>
                <p>The word was not found, sorry!</p>
                <%
            }

        }
        
        // Request or no request, we show them the form to submit a new request
        %>
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
        // Database has not been populated, we need to populate it
        %>
            <p>Unfortunately, the database hasn't been populated yet :(</p>
        <%
    }
    %>

<a href="conjugator.jsp">Conjugator!</a>

</body>
</html>