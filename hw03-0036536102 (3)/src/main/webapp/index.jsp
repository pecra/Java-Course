<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session ="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
<style>
  body{
    background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
  }
</style>
</head>
<body>
    <a href="./colors.jsp">Background color chooser</a>
    <hr>
    <form action="trigonometric" method="GET">
      Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
      Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
      <input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
    </form>
    <hr>
    <a href="stories/funny.jsp">Funny Story</a>
    <hr>
    <a href="report.jsp">OS usage report</a>
    <hr>
    <a href="powers?a=1&b=100&n=3">Download power table</a>
    <hr>
    <a href="appinfo.jsp">App info</a>
    <hr>
    <a href="glasanje">Glasanje</a>
</body>
</html>