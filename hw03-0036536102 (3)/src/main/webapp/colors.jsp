<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session ="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Colors</title>
<style>
  body{
    background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
  }
</style>
</head>
<body>
  <h1>Colors:</h1>
  <ul>
    <li><a href="setcolor?color=white">WHITE</a></li>
    <li><a href="setcolor?color=red">RED</a></li>
    <li><a href="setcolor?color=green">GREEN</a></li>
    <li><a href="setcolor?color=cyan">CYAN</a></li>
    </ul>
   <a href="./index.jsp">Home</a>
</body>
</html>