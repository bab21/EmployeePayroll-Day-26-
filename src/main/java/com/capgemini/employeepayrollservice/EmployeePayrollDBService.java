package com.capgemini.employeepayrollservice;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class EmployeePayrollDBService {
	
	public List<EmployeePayrollData> readData(){
		String sql="SELECT * FROM employee_payroll";
		List<EmployeePayrollData> employeePayrollList=new ArrayList<>();
		try {
			Connection connection =this.getConnection();
			Statement statement=connection.createStatement();
			ResultSet result=statement.executeQuery(sql);
			while(result.next()) {
				int id=result.getInt("id");
				String name=result.getString("name");
				double salary=result.getDouble("salary");
				LocalDate start=result.getDate("start").toLocalDate();
				char gender=result.getString("gender").charAt(0);
				employeePayrollList.add(new EmployeePayrollData(id,name,salary,start,gender));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	private Connection getConnection() throws SQLException{
		String jdbcURL="jdbc:mysql://localhost:3306/payroll_service?allowPublicKeyRetrieval=true&useSSL=false";
		String userName="root";
		String password="nancy21@Bab";
		Connection connection;
	    System.out.println("Connecting to database:"+jdbcURL);
		connection=DriverManager.getConnection(jdbcURL,userName,password);
		System.out.println("Connection is successful!!!!"+connection);
		return connection;
	}
	private static void listDrivers() {
		Enumeration<Driver> driverList=DriverManager.getDrivers();
		while(driverList.hasMoreElements()) {
			Driver driverClass=(Driver) driverList.nextElement();
			System.out.println("  "+driverClass.getClass().getName());
		}
	}

}
