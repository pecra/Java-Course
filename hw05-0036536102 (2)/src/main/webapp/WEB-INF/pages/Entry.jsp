<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>

    <title>Entries</title>
    
    <style type="text/css">
        .container {
        border: 1px solid #ccc;
        padding: 10px;
    }
        .error {
            color: #FF0000;
        }

        .formLabel {
            display: inline-block;
            width: 100px;
            font-weight: bold;
            text-align: right;
            padding-right: 10px;
        }

        .formControls {
            margin-top: 10px;
        }
    </style>
</head>
  <body>
     <c:if test="${sessionScope[\"current.user.id\"] !=null}">
        <h6>${sessionScope["current.user.fn"]} ${sessionScope["current.user.ln"]}</h6>
        <h6><a href="<%=request.getContextPath()%>/servleti/logout"> Odjava</a></h6>
        <br>
    </c:if>
    <c:if test="${sessionScope[\"current.user.id\"] ==null}">
        <p>Niste prijavljeni!</p>
        <br>
    </c:if>
    <hr>
      <h1><u>${entry.getTitle()}</u></h1>
      <div class="container">
        <p>${entry.getText()}</p>
      </div>
      <br>
      <hr>
    <h4>Komentari: </h4>
      <c:forEach items="${entry.comments}" var="comment">
        <h4>${comment.usersEMail}</h4>
        <h5>${comment.postedOn}</h5>
        <div>${comment.message}</div>
        <hr>
    </c:forEach>

    <h3>Napisi komentar:</h3>
    <form action="<%=request.getContextPath()%>/servleti/author/${author.nick}/${entry.id}" method="post">
    <div>
            <span class="formLabel">Tekst</span>
            <textarea type="text" name="comment" rows="15" cols="50"></textarea>
    </div>
    <div class="formControls">
        <span class="formLabel">&nbsp;</span>
        <input type="submit" name="method" value="Komentiraj">
    </div>

</form>

<br>
    
<br>     
  <c:if test="${sessionScope[\"current.user.id\"].equals(author.id)}">  
        <h6><a href="${action}/${entry.getId()}">Uredi</a></h6>
        <br>
  </c:if>
  <h6><a href="<%=request.getContextPath()%>/servleti/main"> Početna</a></h6>
  </body>
</html>