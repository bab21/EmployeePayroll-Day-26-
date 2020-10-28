package com.capgemini.employeepayrollservice;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class EmployeePayrollDBService {
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService empployeePayrollDBService=null;
	
		  
    // private constructor restricted to this class itself 
    private EmployeePayrollDBService() 
    { 
    	
    } 
  
    // static method to create instance of Singleton class 
    public static EmployeePayrollDBService getInstance() 
    { 
        if (empployeePayrollDBService== null) 
        	empployeePayrollDBService = new EmployeePayrollDBService (); 
  
        return empployeePayrollDBService;
    } 
	
	public List<EmployeePayrollData> readData() throws EmployeePayrollException{
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
			throw new EmployeePayrollException("unable to read data from database");
		}
		return employeePayrollList;
	}
	public int updateEmployeeData(String name,double salary) throws EmployeePayrollException {
		return this.updateEmployeeDataUsingPreparedStatement(name, salary);
	}
	private int updateEmployeeDataUsingStatement(String name,double salary) throws EmployeePayrollException {
		String sql=String.format("update employee_payroll set salary=%.02f where name='%s';",salary,name);
		try(Connection connection=this.getConnection()){
			Statement statement=connection.createStatement();
			return statement.executeUpdate(sql);
		}
		catch(SQLException e) {
			throw new EmployeePayrollException("unable to connect to database");
		}
	}
	private int updateEmployeeDataUsingPreparedStatement(String name,double salary) throws EmployeePayrollException{
		try {
			Connection connection=this.getConnection();
			String sql=String.format("UPDATE employee_payroll SET salary=%.2f WHERE name='%s' ;", salary, name);
			
			PreparedStatement preparedStatement=connection.prepareStatement(sql);			
			return preparedStatement.executeUpdate(sql);
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new EmployeePayrollException("unable to create prepared statement");
		}
	}
	private Connection getConnection() throws SQLException{
		listDrivers();
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
	public List<EmployeePayrollData> getEmployeePayrollData(String name) throws EmployeePayrollException {
		// TODO Auto-generated method stub
		List<EmployeePayrollData> employeePayrollDataList=null;
		if(this.employeePayrollDataStatement==null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet=employeePayrollDataStatement.executeQuery();
			employeePayrollDataList=this.getEmployeePayrollData(resultSet);
		}
		catch(SQLException e) {
			throw new EmployeePayrollException("unable to execute query");
		}
		return employeePayrollDataList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) throws EmployeePayrollException {
		List<EmployeePayrollData> employeePayrollDataList=new ArrayList<>();
		try {
			while(resultSet.next()) {
				int id=resultSet.getInt("id");
				String name=resultSet.getString("name");
				double salary=resultSet.getDouble("salary");
				LocalDate start=resultSet.getDate("start").toLocalDate();
				char gender=resultSet.getString("gender").charAt(0);
				employeePayrollDataList.add(new EmployeePayrollData(id,name,salary,start,gender));
			}
		}
		catch(SQLException e) {
			throw new EmployeePayrollException("unable to read data from database");
		}
		return employeePayrollDataList;
	}

	private void prepareStatementForEmployeeData() throws EmployeePayrollException{
		try {
			Connection connection=this.getConnection();
			String sql="SELECT * FROM employee_payroll WHERE name=?";
			employeePayrollDataStatement=connection.prepareStatement(sql);
		}
		catch(SQLException e) {
			throw new EmployeePayrollException("unable to prepare statement");
		}
	}

	public List<EmployeePayrollData> getEmployeePayrollDataWithStartDateInGivenRange() throws EmployeePayrollException {
		// TODO Auto-generated method stub
		List<EmployeePayrollData> employeePayrollList=new ArrayList<>();

		try {
			Connection connection=this.getConnection();
			String sql="select * from employee_payroll where start BETWEEN CAST('2019-01-01' AS DATE) and DATE(NOW());";
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			employeePayrollList=getEmployeePayrollData(resultSet);
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}
}
