<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>

    <title>Registracija</title>
    
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
    <c:when test="${success!= null}">
      Uspjesna prijava!
    </c:when>
    <c:otherwise>
      <h1>Registracija</h1>
      
      <form action="register" method="post">
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

            <div>
                <div>
                    <span class="formLabel">Ime</span><input type="text" name="firstName" value='<c:out
                value="${rf.firstName}"/>' size="20">
                </div>
            </div>

            <div>
                <div>
                    <span class="formLabel">Prezime</span><input type="text" name="lastName" value='<c:out
                value="${rf.lastName}"/>' size="20">
                </div>
            </div>

            <div>
                <div>
                    <span class="formLabel">E-Mail</span><input type="email" name="email" value='<c:out
                value="${rf.email}"/>' size="20">
                </div>
            </div>

            <div class="formControls">
                <span class="formLabel">&nbsp;</span>
                <input type="submit" name="method" value="Registiraj se">
            </div>

        </form>
        
    </c:otherwise>
  </c:choose>
  <h1><a href="main"> Početna</a></h1>
  </body>
</html>