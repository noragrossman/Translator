<!DOCTYPE html>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
    <a href="translateHome.jsp">Home</a>
    	
    <h1>Datastore Manager</h1>

    <h4>Let's try to populate the datastore...</h4>
    <p>Enter <b>"spanish"</b>, <b>"english"</b> or <b>"translation"</b> in the box below depending on which table you would like to populate (all lowercase)</p>
    <form action="/populate" method="post">
        <input type="text" name="mode" />
        <input type="submit" value="Go!" />
    </form>
</body>
</html>