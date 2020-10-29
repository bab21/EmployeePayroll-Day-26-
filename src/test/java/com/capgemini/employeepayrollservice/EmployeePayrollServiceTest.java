package com.capgemini.employeepayrollservice;

import static org.junit.Assert.*;


import java.util.*;

import org.junit.Test;

import com.capgemini.employeepayrollservice.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps= {
				new EmployeePayrollData(1,"Babli",30000),
				new EmployeePayrollData(2,"Nancy",40000),
				new EmployeePayrollData(3,"Sagar",50000)
				};
		
		EmployeePayrollService employeePayrollService=new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
		employeePayrollService.printData(IOService.FILE_IO);
		long numberOfEmployees=employeePayrollService.countNumberOfEmployees();
		assertEquals(3,numberOfEmployees);
	}
	@Test
	public void givenFileOnReadingFromFilesShouldMatchEmployeeCount() throws EmployeePayrollException {
		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDataList=employeePayrollService.readEmployeePayrollData(IOService.FILE_IO);
		System.out.println( employeePayrollDataList);
		
		assertEquals(3,employeePayrollDataList.size());
	}
	
	//UC2 Database..
	@Test
	public void givenEmployeePayrollDB_WhenRetrieved_ShouldMatchEmployeeCount() throws EmployeePayrollException {
		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDataList=employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		assertEquals(3,employeePayrollDataList.size());
	}
	//UC3 Database...
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws EmployeePayrollException {
		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDataList=employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terisa",300000.00);
		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		assertTrue(result);
		
	}
	
	//UC5....
	@Test
	public void givenDateRange_WhenEmployeeDataRetrieved_ShouldMatch() throws EmployeePayrollException {
		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDataList=employeePayrollService.getEmployeeWithDateRange();
		boolean result=employeePayrollDataList.get(0).name.equals("Terisa") && employeePayrollDataList.get(1).name.equals("Charlie");
		assertTrue(result);	
	}
	//UC6...
	@Test
	public void givenEmployeeData_WhenSumSalaryWithGender_ShouldMatch() throws EmployeePayrollException {
		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
		double salarySum=employeePayrollService.getEmployeeSalarySumGroupWithGender();
		assertEquals(600000.0,salarySum,0.0);
	}

}
