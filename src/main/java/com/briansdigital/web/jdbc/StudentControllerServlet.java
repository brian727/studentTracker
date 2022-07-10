package com.briansdigital.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentDButil studentDButil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		super.init();
		//create student db util and pass in conn pool 
		try {
			studentDButil = new StudentDButil(dataSource);
		} catch (Exception exc) {
			throw new ServletException(exc);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
				//read the "command" parameter
				String theCommand = request.getParameter("command");
				//if command is missing, default to student list
				if (theCommand == null ) {
					theCommand = "LIST";
					}
				// route to the appropriate method
				switch(theCommand) {
				case "LIST":
					listStudents(request, response);
					break;
					
				case "LOAD":
					loadStudent(request, response);
					break;
					
				case "UPDATE":
					updateStudent(request, response);
					break;
					
				case "DELETE":
					deleteStudent(request, response);
					break;
					
				default: listStudents(request, response);
				}
		
			} catch (Exception exc) {
				throw new ServletException(exc);
			}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student id
		String theStudentId = request.getParameter("studentId");
		
		// delete student
		studentDButil.deleteStudent(theStudentId);
		
		// send user back to student list
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
			// read student info
			int id = Integer.parseInt(request.getParameter("studentId"));
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
		
			// create new student object
			Student theStudent = new Student(id, firstName, lastName, email);
			// perform update on db
			studentDButil.updateStudent(theStudent);
			// send user back to list students page
			listStudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read student id from data
		String theStudentId = request.getParameter("studentId");
		
		//get student from db util
		Student theStudent = studentDButil.getStudent(theStudentId);
		
		//place student in request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read student info from form
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create new student object
		Student theStudent = new Student(firstName, lastName, email);
		// add student to db
		studentDButil.addStudent(theStudent);
		
		//send back to student list
		//send as redirect to avoud mult browser reload issue
		response.sendRedirect(request.getContextPath()+"/StudentControllerServlet?command=LIST");
		
	
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) 
		throws Exception {	
		// get students from db util
		List<Student> students = studentDButil.getStudents();
		//add students to the request object
		request.setAttribute("STUDENT_LIST", students);
		//send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // read the "command" parameter
            String theCommand = request.getParameter("command");
                    
            // route to the appropriate method
            switch (theCommand) {
                            
            case "ADD":
                addStudent(request, response);
                break;
                                
            default:
                listStudents(request, response);
            }
                
        }
        catch (Exception exc) {
            throw new ServletException(exc);
        }
        
    }


	

}
