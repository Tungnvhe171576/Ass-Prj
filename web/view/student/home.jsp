<%-- 
    Document   : home
    Created on : Jul 2, 2024, 1:53:18 PM
    Author     : KEISHA
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body> 
        <h1> Hi student ${sessionScope.user.displayname}</h1>
        
        <a href="../../subject/list"> FLM FPT</a> <br/>
        
        <a href="../../student/status"> ACADEMIC TRANSCRIPT</a>
    </body>
</html>
