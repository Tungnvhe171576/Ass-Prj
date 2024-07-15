/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Assessment;
import model.Course;
import model.Exam;
import model.Grade;
import model.Student;

/**
 *
 * @author sonnt-local
 */
public class GradeDBContext extends DBContext<Grade> {

    public ArrayList<Grade> getGradesFromExamIds(ArrayList<Integer> eids) {
        ArrayList<Grade> grades = new ArrayList<>();
        PreparedStatement stm = null;
        try {
            String sql = "SELECT eid,sid,score FROM grades WHERE (1>2)";
            for (Integer eid : eids) {
                sql += " OR eid = ?";
            }

            stm = connection.prepareStatement(sql);

            for (int i = 0; i < eids.size(); i++) {
                stm.setInt((i + 1), eids.get(i));
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Grade g = new Grade();
                g.setScore(rs.getFloat("score"));

                Student s = new Student();
                s.setId(rs.getInt("sid"));
                g.setStudent(s);

                Exam e = new Exam();
                e.setId(rs.getInt("eid"));
                g.setExam(e);

                grades.add(g);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GradeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(GradeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return grades;
    }

    public void insertGradesForCourse(int cid, ArrayList<Grade> grades) {
        String sql_remove = "DELETE grades WHERE sid IN (SELECT sid FROM students_courses WHERE cid = ?)";
        String sql_insert = "INSERT INTO [grades]\n"
                + "           ([eid]\n"
                + "           ,[sid]\n"
                + "           ,[score])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?)";
        
        PreparedStatement stm_remove =null;
        ArrayList<PreparedStatement> stm_inserts = new ArrayList<>();
        
        try {
            connection.setAutoCommit(false);
            stm_remove = connection.prepareStatement(sql_remove);
            stm_remove.setInt(1, cid);
            stm_remove.executeUpdate();
            
            for (Grade grade : grades) {
                PreparedStatement stm_insert = connection.prepareStatement(sql_insert);
                stm_insert.setInt(1, grade.getExam().getId());
                stm_insert.setInt(2,grade.getStudent().getId());
                stm_insert.setFloat(3, grade.getScore());
                stm_insert.executeUpdate();
                stm_inserts.add(stm_insert);
            }
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(GradeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(GradeDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        finally
        {
            try {
                connection.setAutoCommit(true);
                stm_remove.close();
                for (PreparedStatement stm_insert : stm_inserts) {
                    stm_insert.close();
                }
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(GradeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
     public ArrayList<Grade> getGradeByStudentID(int stdID) {
        ArrayList<Grade> grades = new ArrayList<>();

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT st.sid, st.sname, s.subid, c.cid, c.cname, "
                    + "CASE "
                    + "    WHEN COUNT(g.score) < (SELECT COUNT(*) FROM assesments a WHERE a.subid = s.subid) "
                    + "    THEN 0 "
                    + "    ELSE AVG(g.score) "
                    + "END AS Average "
                    + "FROM students st "
                    + "LEFT JOIN students_courses sc ON st.sid = sc.sid "
                    + "LEFT JOIN courses c ON sc.cid = c.cid "
                    + "LEFT JOIN subjects s ON c.subid = s.subid "
                    + "LEFT JOIN exams e ON e.aid IN (SELECT a.aid FROM assesments a WHERE a.subid = s.subid) "
                    + "LEFT JOIN grades g ON g.eid = e.eid AND g.sid = st.sid "
                    + "WHERE st.sid = ? "
                    + "GROUP BY st.sid, st.sname, s.subid, c.cid, c.cname";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, stdID);
            rs = stm.executeQuery();
            while (rs.next()) {
                Grade g = new Grade();
                g.setScore(rs.getFloat("Average"));

                Student s = new Student();
                s.setId(rs.getInt("sid"));
                s.setName(rs.getString("sname"));

                Course c = new Course();
                c.setId(rs.getInt("cid"));
                c.setName(rs.getString("cname"));

                ArrayList<Course> courses = new ArrayList<>();
                courses.add(c);
                s.setCourses(courses);

                g.setStudent(s);
                grades.add(g);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GradeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grades;
    }

    public ArrayList<Grade> getGradeByCidAndSid(int courseID, int studentID) {
        PreparedStatement stm = null;
        ArrayList<Grade> grades = new ArrayList<>();
        try {
            String sql = "SELECT a.aname, a.weight, g.score\n"
                    + "FROM students st\n"
                    + "LEFT JOIN students_courses sc ON st.sid = sc.sid\n"
                    + "LEFT JOIN courses c ON sc.cid = c.cid\n"
                    + "LEFT JOIN assesments a ON c.subid = a.subid\n"
                    + "LEFT JOIN exams e ON e.aid = a.aid\n"
                    + "LEFT JOIN grades g ON g.eid = e.eid AND g.sid = st.sid\n"
                    + "WHERE st.sid = ? AND c.cid = ?;\n";

            stm = connection.prepareStatement(sql);
            stm.setInt(1, studentID);
            stm.setInt(2, courseID);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Grade grade = new Grade();
                grade.setScore(rs.getFloat("score"));

                Assessment assessment = new Assessment();
                assessment.setName(rs.getString("aname"));
                assessment.setWeight(rs.getFloat("weight"));

                Exam exam = new Exam();
                exam.setAssessment(assessment);
                grade.setExam(exam);

                grades.add(grade);
            }

            rs.close();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GradeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grades;
    }

    @Override
    public void insert(Grade model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Grade model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Grade model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Grade get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Grade> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ArrayList<Grade> getGradesByStudentId(int studentId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
