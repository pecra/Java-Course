<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OS usage</title>
<style>
  body{
    background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
  }
</style>
</head>
<body>
  <h1>OS usage</h1>
  <p>Here are the results of OS usage in survey that we completed.</p>
  <img src="./reportImage" alt="OS usage chart">
  <a href="./index.jsp">Home</a>

</body>
</html>