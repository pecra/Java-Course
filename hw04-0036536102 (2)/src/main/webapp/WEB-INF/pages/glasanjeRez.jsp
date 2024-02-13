<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.Map" %>
    <%@ page import="java.util.List" %>
    <%@ page import="hr.fer.oprpp2.p08.Data" %>
<!DOCTYPE html>
<html>
 <head>
 </head>
 <body>

 <h1>Rezultati glasanja</h1>
 <p>Ovo su rezultati glasanja.</p>
 <table border="1" cellspacing="0" class="rez">
 <thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
 <tbody>
    <% List<Data> rezultati = (List<Data>)request.getAttribute("data");
       Data prvi = rezultati.get(0);
       Data drugi= rezultati.get(1);
       for(Data d : rezultati){
     %>
    <tr><td><%=d.getTitle()%></td><td><%=d.getVotes() %></td></tr>
  <% } %>
 </tbody>
 </table>

 <h2>Grafički prikaz rezultata</h2>
 <img alt="Pie-chart" src="glasanje-grafika?pollID=<%=request.getAttribute("pollID")%>" width="600" height="600" />

 <h2>Rezultati u XLS formatu</h2>
 <p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls?pollID=<%=request.getAttribute("pollID")%>">ovdje</a></p>

 <h2>Razno</h2>
 <p>Primjeri pjesama pobjedničkih bendova:</p>
 <ul>
 <li><a href=<%=prvi.getLink() %> target="_blank"><%=prvi.getTitle() %></a></li>
 <li><a href=<%=drugi.getLink() %> target="_blank"><%=drugi.getTitle() %></a></li>
 </ul>
 <a href="./index.html">Home</a>
 </body>
</html>