<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ page import="java.util.Map" %>
    <%@ page import="java.util.List" %>
    <%@ page import="hr.fer.oprpp2.p08.Data" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Glasanje</title>
</head>
   <body>
     <ol>
       <% List<Data> bendovi = (List<Data>) request.getAttribute("data");
          for(Data b : bendovi){ %>
       <li><a href="glasanje-glasaj?id=<%= b.getId()%>&pollID=<%= request.getAttribute("pollID")%>"><%= b.getTitle()%></a></li>
       <%} %>
     </ol>
   </body>
</html>