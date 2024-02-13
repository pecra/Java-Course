<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>

    <title>Main</title>
    
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

  <c:choose>
    <c:when test="${sessionScope[\"current.user.id\"] !=null}">
        <h1>Vaše ime: ${sessionScope["current.user.fn"]}</h1>
        <h1>Vaše prezime: ${sessionScope["current.user.ln"]}</h1>
        <h1><a href="<%=request.getContextPath()%>/servleti/logout"> Odjava</a></h1>
        <br>
    </c:when>
    <c:otherwise>
      <h1>Login</h1>
      
      <form action="main" method="post">
      
            <div>
                <div>
                    <span class="formLabel">Nadimak</span><input type="text" name="nick" value='<c:out
                value="${rf.nick}"/>' size="15">
                </div>
            </div>

            <div>
                <div>
                    <span class="formLabel">Lozinka</span><input type="password" name="password" value='' size="20">
                </div>
            </div>
            
            <div class="formControls">
                <span class="formLabel">&nbsp;</span>
                <input type="submit" name="method" value="Ulogiraj se">
            </div>

        </form>
        <hr>
        <h1><a href="register"> Registriraj se</a></h1>
        
        
    </c:otherwise>
  </c:choose>
  <hr>
  <h2>Autori:</h2>
        <ul>
          <c:forEach items="${regAuthors}" var="regAuthors">
             <li><a href="author/${regAuthors.nick}">${regAuthors.firstName} ${regAuthors.lastName}</a></li>
          </c:forEach>
        </ul>

  </body>
</html>