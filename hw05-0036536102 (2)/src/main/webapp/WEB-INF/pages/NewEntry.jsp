<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>

    <title>Objava</title>
    
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
     <c:if test="${error != null}">
    <div class="error">${error}</div>
</c:if>

<c:if test="${sessionScope[\"current.user.id\"] !=null}">
        <h1>${sessionScope["current.user.fn"]} ${sessionScope["current.user.ln"]}</h1>
        <h1><a href="<%=request.getContextPath()%>/servleti/logout"> Odjava</a></h1>
        <br>
    </c:if>
    <c:if test="${sessionScope[\"current.user.id\"] ==null}">
        <h1>Niste prijavljeni!</h1>
        <br>
    </c:if>

  <c:choose>
    <c:when test="${accDen!= null}">
      ${accDen}
    </c:when>
    <c:otherwise>
      <h1>Objava</h1>
      
      <form action="${action}" method="post">
            <div>
                <div>
                    <span class="formLabel">Naslov</span><input type="text" name="title" value="${entry.title}" size="30">
                </div>
            </div>

            <div>
                <div>
                    <span class="formLabel">Text</span><textarea type="text" name="text" rows="20"
                    cols="40">${entry.text}</textarea>
                </div>
            </div>

            <div class="formControls">
                <span class="formLabel">&nbsp;</span>
                <input type="submit" name="method" value="Spremi">
            </div>

        </form>
        
    </c:otherwise>
  </c:choose>
  <h1><a href="<%=request.getContextPath()%>/servleti/main"> Početna</a></h1>
  </body>
</html>