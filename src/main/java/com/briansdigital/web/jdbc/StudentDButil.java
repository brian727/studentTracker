package com.briansdigital.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDButil {
	private DataSource dataSource;
	
	public StudentDButil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			//get connection
			myConn = dataSource.getConnection();
			
			//create sql statement
			String sql = "select * from student order by last_name";
			myStmt = myConn.createStatement();
			
			//execute query
			myRs = myStmt.executeQuery(sql);
			
			//process result set
			while (myRs.next()) {
				//retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_Name");
				String lastName = myRs.getString("last_Name");
				String email = myRs.getString("email");
				
				//create new student object
				Student tempStudent = new Student(id, firstName, lastName, email);
				
				//add to list of students 
				students.add(tempStudent);
			}
			
			return students;
		}
		finally {
			//close jdbc objects
			close(myConn, myStmt, myRs);
		}
	
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myRs != null) {
				myRs.close();
			}
			if (myStmt !=null) {
				myStmt.close();
			}
			if (myConn !=null) {
				myConn.close(); //doesnt really close rather returns to connection pool
			}
		} 
		
		catch (Exception exc){
			exc.printStackTrace();
		}
		
	}

	

	public void addStudent(Student theStudent) throws Exception {

	
		Connection myConn = null;
		PreparedStatement myStmt = null; 
		
		try {
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql statement for insert
			String sql = "insert into student" + "(first_name, last_name, email)" + "values (?,?,?)";
			myStmt=myConn.prepareStatement(sql);
			
			//set the param values for the student
			myStmt.setString(1,  theStudent.getFirstName());
			myStmt.setString(2,  theStudent.getLastName());
			myStmt.setString(3,  theStudent.getEmail());
			
			//execute sql insert
			myStmt.execute();
			
			} finally {
				//clean up jdbc objects
				close(myConn, myStmt, null);
		}
	}

	public Student getStudent(String theStudentId) throws Exception {
		Student theStudent = null;

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int StudentId;
		
		try {
			//convert student id to int
			StudentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn = dataSource.getConnection();
			
			//create sql to get the selected student 
			String sql = "select * from student where id=?";
			
			//create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1,  StudentId);
			
			//execute statement
			myRs = myStmt.executeQuery();
			
			//retrieve data from result set row
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//use the student id in constructor
				theStudent = new Student(StudentId, firstName, lastName, email);
			} else {
				throw new Exception("Could not find student if:" + StudentId);
			}
			
			return theStudent;
		}
		finally {
			
			//clean up jdbc objects 
			close(myConn, myStmt, myRs);
		}
	}

	public void updateStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			
			
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql update statement
			String sql = "update student " 
						+ "set first_name=?, last_name=?, email=? " 
						+ "where id=?";
			
			//prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params for statement
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			//execute sql statement
			myStmt.execute();
			}
				finally {
					//clean up jdbc objects
					close(myConn, myStmt, null);
			}
		}

	public void deleteStudent(String theStudentId) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			//convert student id to int
			int studentId = Integer.parseInt(theStudentId);
			
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql statement to delete
			String sql = "delete from student where id=?";
			
			//prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1,  studentId);
			
			//execute sql statement
			myStmt.execute();
			
		} finally {
			//clean up jdbc objects 
			close(myConn, myStmt, null);
		}
		
	}
	}

































