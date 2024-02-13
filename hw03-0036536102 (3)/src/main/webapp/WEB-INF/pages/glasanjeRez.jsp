<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.Map" %>
    <%@ page import="java.util.List" %>
    <%@ page import="hr.fer.zemrsi.java.servlets.GlasanjeServlet.Band" %>
<!DOCTYPE html>
<html>
 <head>
 <style type="text/css">
 table.rez td {text-align: center;}
 body{
    background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
  }
 </style>
 </head>
 <body>

 <h1>Rezultati glasanja</h1>
 <p>Ovo su rezultati glasanja.</p>
 <table border="1" cellspacing="0" class="rez">
 <thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
 <tbody>
    <% Map<String,String> rezultati = (Map<String,String>)request.getAttribute("results");
     List<Band> bendovi = (List<Band>) request.getAttribute("bands");
     Band prvi = null;
     Band drugi = null;
     int i = 0;
     for(Map.Entry<String, String> e : rezultati.entrySet()) { 
         String name = "";
         for(Band b : bendovi){
           if(b.getId().equals(e.getKey())){
               name = b.getName();
               if(i == 1){
            	   i++;
            	   drugi = b;
               }
               if(i == 0){
            	   i++;
            	   prvi = b;
               }
           }
         } 
     %>
    <tr><td><%=name%></td><td><%= e.getValue() %></td></tr>
  <% } %>
 </tbody>
 </table>

 <h2>Grafički prikaz rezultata</h2>
 <img alt="Pie-chart" src="./glasanje-grafika" width="600" height="600" />

 <h2>Rezultati u XLS formatu</h2>
 <p>Rezultati u XLS formatu dostupni su <a href="./glasanje-xls">ovdje</a></p>

 <h2>Razno</h2>
 <p>Primjeri pjesama pobjedničkih bendova:</p>
 <ul>
 <li><a href=<%=prvi.getLink() %> target="_blank"><%=prvi.getName() %></a></li>
 <li><a href=<%=drugi.getLink() %> target="_blank"><%=drugi.getName() %></a></li>
 </ul>
 <a href="./index.jsp">Home</a>
 </body>
</html>