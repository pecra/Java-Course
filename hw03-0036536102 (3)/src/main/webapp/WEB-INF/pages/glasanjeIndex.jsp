<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ page import="java.util.Map" %>
    <%@ page import="java.util.List" %>
    <%@ page import="hr.fer.zemrsi.java.servlets.GlasanjeServlet.Band" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Glasanje</title>
<style>
  body{
    background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
  }
</style>
</head>
   <body>
     <h1>Glasanje za omiljeni bend:</h1>
     <p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste
     glasali!</p>
     <ol>
       <% List<Band> bendovi = (List<Band>) request.getAttribute("bands");
          for(Band b : bendovi){ %>
       <li><a href="glasanje-glasaj?id=<%= b.getId()%>"><%= b.getName()%></a></li>
       <%} %>
     </ol>
   </body>
</html>