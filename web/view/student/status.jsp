<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Student Grades</title>
</head>
<body>
    <h1>Grades of ${requestScope.name}</h1>
    <table border="1">
        <tr>
            <th>SUBJECT</th>
            <th>GRADE</th>
            <th>STATUS</th>
            <th>      </th>
        </tr>
        <c:forEach items="${grades}" var="g">
            
            <tr>
                <td>${g.student.courses[0].name}</td>
                <td>${g.score}</td>
                <td>
                    <c:choose>
                        <c:when test="${g.score eq 0}">
                            STUDYING
                        </c:when>
                        <c:when test="${g.score >= 4}">
                            PASSED
                        </c:when>
                        <c:otherwise>
                            NOT PASS
                        </c:otherwise>
                    </c:choose>
                </td>
                <td> <a href="mark?cid=${g.student.courses[0].id}">   MORE </a></td>
                
            </tr>
        </c:forEach>
    </table>
</body>
</html>
