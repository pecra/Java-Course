<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%@ page import="hr.fer.zemrsi.java.servlets.TrigonometricServlet" %>
<%@ page import="hr.fer.zemrsi.java.servlets.TrigonometricServlet.Angle" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trigonometric</title>
<style>
  body{
    background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
  }
</style>
</head>
<body>
  <table>
    <tr>
      <th>Kut</th>
      <th>Sin</th>
      <th>Cos</th>
    </tr>
    <% for(Angle a : (List<Angle>) request.getAttribute("anglesTable")) { %>
      <tr>
        <td><%=a.getAngle()%></td>
        <td><%=a.getSin()%></td>
        <td><%=a.getCos()%></td>
      </tr>
    <% } %>  
  </table>

  <a href="./index.jsp">Home</a>
</body>
</html>