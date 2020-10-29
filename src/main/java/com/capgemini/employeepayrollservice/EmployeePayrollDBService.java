package com.capgemini.employeepayrollservice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService empployeePayrollDBService=null;
	
    private EmployeePayrollDBService() 
    { 
    	
    } 
    public static EmployeePayrollDBService getInstance() 
    { 
        if (empployeePayrollDBService== null) 
        	empployeePayrollDBService = new EmployeePayrollDBService (); 
  
        return empployeePayrollDBService;
    } 
	
	public List<EmployeePayrollData> readData() throws EmployeePayrollException{
		String sql="SELECT * FROM employee_payroll";
		return this.getEmployeePayrollDataUsingDB(sql);
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
	
	private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) throws EmployeePayrollException{
		List<EmployeePayrollData> employeePayrollList=new ArrayList<>();
		try(Connection connection =this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			employeePayrollList= getEmployeePayrollData(resultSet);
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new EmployeePayrollException("unable to read data from database");
		}
		return employeePayrollList;
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
			e.printStackTrace();
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

	public List<EmployeePayrollData> getEmployeePayrollDataFortDateRange(LocalDate startDate,LocalDate endDate) throws EmployeePayrollException {
		String sql;
		sql=String.format("select * from employee_payroll where start BETWEEN '%s' and '%s' ;",Date.valueOf(startDate),Date.valueOf(endDate));
		return this.getEmployeePayrollDataUsingDB(sql);
	}

	public Map<String, Double> getEmployeeSalarySumGroupWithGender() throws EmployeePayrollException {
		String sql="select gender,sum(salary) as salary_sum from employee_payroll group by gender;";
		Map<String,Double> genderToSalarySum=new HashMap<>();
		try(Connection connection=this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next()) {
				String gender=resultSet.getString("gender");
				double salary=resultSet.getDouble("salary_sum");
				genderToSalarySum.put(gender, salary);
				
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return genderToSalarySum;
	}
	public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
		int employeeId=-1;
		EmployeePayrollData employeePayrollData=null;
		String sql=String.format("INSERT INTO employee_payroll(name,gender,salary,start)"+
		              "VALUES ('%s','%s','%s','%s' )", name,gender,salary,Date.valueOf(startDate));
		try(Connection connection=this.getConnection()){
			Statement statement=connection.createStatement();
			int rowAffected=statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
			if(rowAffected==1) {
				ResultSet resultSet=statement.getGeneratedKeys();
				if(resultSet.next()) employeeId=resultSet.getInt(1);
			}
			employeePayrollData=new EmployeePayrollData(employeeId,name,salary,startDate,gender.charAt(0));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollData;
	}
}
