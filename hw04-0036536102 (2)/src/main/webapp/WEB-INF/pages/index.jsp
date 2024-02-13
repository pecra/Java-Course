<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ page import="java.util.Map" %>
    <%@ page import="java.util.List" %>
    <%@ page import="hr.fer.oprpp2.p08.Poll" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Odaberite anketu:</title>
</head>
   <body>
     <ol>
       <% List<Poll> data = (List<Poll>) request.getAttribute("polls");
          for(Poll p : data){ %>
       <h1><%=p.getTitle()%></h1>   
       <li><a href="glasanje?pollID=<%= p.getId()%>"><%= p.getLink()%></a></li>
       <%} %>
     </ol>
   </body>
</html>