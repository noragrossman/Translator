<!-- Author Nora Grossman, 2015 -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.ArrayList" %>

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
    	<h3>Spanish Verb Conjugator</h3>

		<form action="/conjugate" method="post">
			<%
				// Here check if this page was reached by linking from showWord.jsp

				String verb = (String)request.getParameter("verb");
				if (verb != null) {
					%>
						<div>Enter a Spanish verb: <input type="text" name="verb" value="<%=verb%>" class="textbox"/>
					<%

				} else {
					%>
						<div>Enter a Spanish verb: <input type="text" name="verb" class="textbox"/>
					<%
				}
			%>

				<select name="tense">
					<option value="0">Present</option>
					<option value="1">Preterit</option>
					<option value="2">Imperfect</option>
					<option value="3">Future</option>
					<option value="4">Conditional</option>
					<option value="5">Present perfect</option>
					<option value="6">Pluperfect</option>
					<option value="7">Future perfect</option>
					<option value="8">Conditional perfect</option>
					<option value="9">Present progressive</option>
					<option value="10">Past progressive</option>
					<option value="11">Present subjunctive</option>
					<option value="12">Imperfect subjunctive</option>
					<option value="13">Present perfect subjunctive</option>
					<option value="14">Pluperfect subjunctive</option>
					<option value="15">Imperative</option>
					<option value="16">Past participle</option>
					<option value="17">Gerund</option>
				</select>
			</div>
			<div>
				<input type="submit" value="Go" class="button"/>
			</div>
		</form>

		<%
		// Get the english and the conjugations from the request, if given
		if (request.getAttribute("infinitive") != null) {
			String infinitive = (String)request.getAttribute("infinitive");
			%>
				<p class="featured"><%=infinitive%></p>
			<%
		}
		
		if (request.getAttribute("conjugations") != null) {
			ArrayList<String> c = (ArrayList<String>)request.getAttribute("conjugations");
			%>
			<table>
				<tr><td><b>yo</b> <%=c.get(0)%></td><td><b>nosotros</b> <%=c.get(3)%></td></tr>
				<tr><td><b>tú</b> <%=c.get(1)%></td><td><b>vosotros</b> <%=c.get(4)%></td></tr>
				<tr><td><b>él</b> <%=c.get(2)%></td><td><b>ellos</b> <%=c.get(5)%></td></tr>
			</table>
			<%
		}
		%>
    </div>


</body>

</html>