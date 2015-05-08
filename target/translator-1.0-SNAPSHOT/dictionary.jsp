<!-- Author Nora Grossman, 2015 -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>

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
            // True for "english mode" (english -> spanish)
            // False for "spanish mode" (spanish -> english)
            boolean eMode = true;

            if (request.getParameter("mode") != null) {
                String mode = (String)request.getParameter("mode");

                if (mode.compareTo("spanish") == 0) {
                    eMode = false;
                }
            }
            
            String to;
            String from;
            if (eMode) {
                to = "spanish";
                from = "english";
                %>
                    <h3 class="heading">English-Spanish Dictionary</h3>
                <%
            } else {
                to = "english";
                from = "spanish";
                %>
                    <h3 class="heading">Spanish-English Dictionary</h3>
                <%
            }

            %>
                <form action="/dictionary" method="post">
                    <div>
                        Enter an <%=from%> word <input type="text" name="w" class="textbox"/>
                        <input type="submit" value="GO" class="button"/>
                        <input type="hidden" name="mode" value=<%=from%>>
                    </div>
                </form>
            <%

            // Get the request attributes, if there are any
            String lemma;
            List<Entity> translations;

            if (request.getAttribute("lemma") != null) {
                lemma = (String)request.getAttribute("lemma");
                %>
                    <p class="featured"><%=lemma%></p>
                <%

                if (request.getAttribute("translations") != null) {
                    translations = (List<Entity>)request.getAttribute("translations");
                    %>
                        <table>
                            <tr><th>part of speech</th><th><%=to%></th></tr>
                            <%
                            for (Entity e : translations) {
                                String url = "showWord.jsp?word=" + e.getProperty(to) + "&pos=" + e.getProperty("partOfSpeech") + "&mode=" + from + "&trans=" +lemma;
                                %>
                                <tr><td><%=e.getProperty("partOfSpeech")%></td>
                                    <td><a href=<%=url%>><%=e.getProperty(to)%></a></td>
                                </tr>
                                <%
                            }
                            %>
                        </table>
                    <%
                } else {
                    if (request.getAttribute("error") != null) {
                        %>
                            <p style="color:red;"><%=request.getAttribute("error")%></p>
                        <%
                    }
                    %>
                        <p><i>Sorry, no translations found</i></p>
                    <%
                }
            }

            if (eMode) {
                %>
                    <a href="dictionary.jsp?mode=spanish" class="toggle">Spanish -> English</a>
                <%

            } else {
                %>
                    <a href="dictionary.jsp?mode=english" class="toggle">English -> Spanish</a>
                <%

            }

        %>

        <div>
            <!--<a href="populate.jsp">Click here to go to management console</a>-->
        </div>
    </div>

    

</body>
</html>