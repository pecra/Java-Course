<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import="java.util.List" %>
 <%@ page import="java.util.Random" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%String[] lista = new String[]{"blue","red","black"};
    Random random = new Random();
    int randomColor = random.nextInt(3);%>
<style>
  body{
    background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
    color : <%= lista[randomColor]%>
  }
</style>
<title>Funny Story</title>
</head>
<body>
<p>
Once upon a time, there was a software engineer who was very frustrated with his computer. He had been working on a coding project for days and his computer kept crashing. In desperation, he decided to call tech support. When the technician asked what the problem was, the engineer replied, 'My computer keeps telling me to press any key, but I can't find the Any key!' The technician tried to explain that the computer was simply asking him to press any key on the keyboard, but the engineer was still confused. Finally, the technician said, 'Just press the letter A on your keyboard.' The engineer responded, 'Oh, okay, I found the A key, but where's the N key?'
</p>
<a href="/webapp1/index.jsp">Home</a>
</body>
</html>