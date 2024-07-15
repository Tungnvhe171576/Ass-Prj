<%-- 
    Document   : mark
    Created on : Jul 7, 2024, 4:16:10 PM
    Author     : KEISHA
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MARK REPORT</title>
    </head>
    <body>
        
        <c:if test="${requestScope.grades ne null}">
            <table border="1px">
                <tr>
                    <td> CATEGORY </td>
                    <td> WEIGHT</td>
                    <td> VALUE</td>
                    
                </tr>
                <c:forEach items="${requestScope.grades}" var="g">
                <tr>
                    <td> ${g.exam.assessment.name}</td>
                    <td> ${g.exam.assessment.weight}</td>
                    <td> 
                        <c:choose>
                            <c:when test="${g.score == 0.0}">
                                
                            </c:when>
                            <c:otherwise>
                                  ${g.score}                              
                            </c:otherwise>
                            
                        </c:choose>
                        
                        
                        
                        
                       
                    
                    </td>
                </tr>
                </c:forEach>
            </table>
            
            
            
            
        </c:if>
        
        
        
    </body>
</html>
