<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Grades</title>
</head>
<body>
    <h2>Your Grades</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Exam</th>
                <th>Score</th>
            </tr>
        </thead>
        <tbody>
            <% for (Grade grade : (ArrayList<Grade>) request.getAttribute("studentGrades")) { %>
            <tr>
                <td><%= grade.getExam().getName() %></td>
                <td><%= grade.getScore() %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>
