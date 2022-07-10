<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
	<title>JSP/JDBC: Student Tracker App</title>
	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<body>
	<div id="wrapper" >
		<div id="header">
			<h2>JSP and JDBC Demo</h2>
			<h3>Student Registration System</h3>
		</div>
	</div>
	<div id="container">
		<div id="content">
		<input type="button" value="Add Student" 
			onclick="window.location.href='add-student-form.jsp'; return false;"
			class="add-student-button"/>
				
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>email</th>
					<th>Action</th>
				</tr>
				
				<c:forEach var="tempStudent" items="${STUDENT_LIST}">
				
					<!-- set up link for each student -->
					<c:url var="tempLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD"/>
						<c:param name="studentId" value="${tempStudent.id}"/>
					</c:url>
					
					<!-- link to delete student -->
					<c:url var="deleteLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE"/>
						<c:param name="studentId" value="${tempStudent.id}"/>
					</c:url>
				
					<tr>
						<td>${tempStudent.firstName} </td>	
						<td>${tempStudent.lastName} </td>
						<td>${tempStudent.email} </td>
						<td>
							<a href="${tempLink}">Update</a>
							|
							<a href="${deleteLink}"
							onclick="if(!(confirm('Are you sure?'))) return false">Delete</a>
						</td>
					</tr>
					
				</c:forEach>
				
				
				
			</table>
		</div>
	</div>

</body>
</html>