///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//
//package controller.exam;
//
//import controller.auth.BaseRequiredStudentAuthenticationController;
//import dal.GradeDBContext;
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
//import model.Grade;
//import model.Student;
//import model.User;
//
///**
// *
// * @author ADMIN
// */
//public class ViewExamController extends BaseRequiredStudentAuthenticationController {
//
//    @Override
////    protected void doGet(HttpServletRequest request, HttpServletResponse response, User user, Student student)
////            throws ServletException, IOException {
////        GradeDBContext gradeDB = new GradeDBContext();
////
////        // Assuming student ID is accessible from the student object
////        int studentId = student.getId();
////
////        // Retrieve grades for the student
////        ArrayList<Grade> grades = gradeDB.getGradesByStudentId(studentId);
////
////        request.setAttribute("grades", grades);
////        request.getRequestDispatcher("../view/exam/view.jsp").forward(request, response);
////    }
//
//protected void doGet(HttpServletRequest request, HttpServletResponse response, User user, Lecturer lecturer)
//        throws ServletException, IOException {
//    // Assuming you have a method to retrieve grades for a student based on user context (student ID)
//    int studentId = user.getId();  // Adjust this based on your User model
//    GradeDBContext gradeDB = new GradeDBContext();
//    ArrayList<Grade> studentGrades = gradeDB.getGradesByStudentId(studentId);
//
//    request.setAttribute("studentGrades", studentGrades);
//    request.getRequestDispatcher("../view/exam/viewGrades.jsp").forward(request, response);
//}
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response, User user, Student student)
//            throws ServletException, IOException {
//        // You can implement POST logic if needed, such as handling form submissions
//        // For viewing purposes, typically doGet is sufficient
//        // Redirect or handle POST as per your application needs
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "View Exam Controller";
//    }
//}
package controller.exam;

import dal.ExamDBContext;
import dal.GradeDBContext;
import dal.StudentDBContext;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Exam;
import model.Grade;
import model.Student;
import model.User;

public class ViewExamController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

   Student student = (Student) request.getSession().getAttribute("student");

        if (student == null) {
            // Redirect to login page if student is not found in session
            response.sendRedirect("../view/auth/login.jsp");
            return;
        }
        // Retrieve grades for the logged-in student
         StudentDBContext stuDB = new StudentDBContext();
        ExamDBContext examDB = new ExamDBContext();
        GradeDBContext gradeDB = new GradeDBContext();
        ArrayList<Grade> studentGrades = gradeDB.getGradesByStudentId(student.getId());
 // ArrayList<Exam> exams = examDB.getExamsByExamIds(eids);
        // Set grades as request attribute to be accessed in JSP
      request.setAttribute("studentGrades", studentGrades);
        
//         request.setAttribute("students", students);
//        request.setAttribute("exams", exams);
//        request.setAttribute("grades",grades);
        request.getRequestDispatcher("/view/exam/viewGrades.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View Exam Controller";
    }
}
