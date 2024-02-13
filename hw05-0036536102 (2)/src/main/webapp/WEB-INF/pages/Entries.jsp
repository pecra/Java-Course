<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>

    <title>Entries</title>
    
    <style type="text/css">
        
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
        <h1>${sessionScope["current.user.fn"]} ${sessionScope["current.user.ln"]}</h1>
        <h1><a href="<%=request.getContextPath()%>/servleti/logout"> Odjava</a></h1>
        <br>
    </c:if>
    <c:if test="${sessionScope[\"current.user.id\"] ==null}">
        <h1>Niste prijavljeni!</h1>
        <br>
    </c:if>
    <hr>
      <h1>Objave autora:</h1>
      <c:forEach items="${author.entries}" var="entries">
             <li><a href="<%=request.getContextPath()%>/servleti/author/${author.nick}/${entries.getId()}">${entries.title}</a></li>
          </c:forEach>
          <hr>
  <c:if test="${sessionScope[\"current.user.id\"].equals(author.id)}">
        <h1><a href="<%=request.getContextPath()%>/servleti/author/${author.nick}/new">Kreiraj novu objavu</a></h1>
    <hr>
    </c:if>
    
    <h1><a href="<%=request.getContextPath()%>/servleti/main"> Početna</a></h1>
  </body>
</html>