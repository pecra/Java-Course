<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
<style>
  body{
    background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
  }
</style>
</head>
<body>
   <h1><%= request.getAttribute("err") %></h1>
   <a href="./powers.jsp">Home</a>
</body>
</html>