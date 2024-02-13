<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>App info</title>
<style>
  body{
    background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
  }
</style>
</head>
<body>
    <h1>App running time</h1>
    <%long startupTime = (long) request.getServletContext().getAttribute("created");
      long timeElapsed = System.currentTimeMillis() - startupTime; 
      long seconds = (timeElapsed / 1000) % 60;
      long minutes = (timeElapsed / (1000 * 60)) % 60;
      long hours = (timeElapsed / (1000 * 60 * 60)) % 24;
      long days = timeElapsed / (1000 * 60 * 60 * 24);
      long millis = timeElapsed % 1000;%>
    <p><%=days%> days, <%=hours%> hours, <%=minutes%> minutes, <%=seconds%> seconds, <%=millis%> millis.</p>
    <a href="./index.jsp">Home</a>
</body>
</html>