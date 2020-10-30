package com.capgemini.employeepayrollservice;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.Test;

import com.capgemini.employeepayrollservice.EmployeePayrollService.IOService;
import com.capgemini.employeepayrollservice.model.EmployeePayrollData;

public class EmployeePayrollServiceTest {
//	//UC2 Database..
//	@Test
//	public void givenEmployeePayrollDB_WhenRetrieved_ShouldMatchEmployeeCount() throws EmployeePayrollException {
//		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
//		List<EmployeePayrollData> employeePayrollDataList=employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
//		System.out.println("data"+employeePayrollDataList);
//		assertEquals(3,employeePayrollDataList.size());
//	}
//	//UC3 Database...
//	@Test
//	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws EmployeePayrollException {
//		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
//		List<EmployeePayrollData> employeePayrollDataList=employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
//		employeePayrollService.updateEmployeeSalary("Terisa",300000.00);
//		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
//		assertTrue(result);
//		
//	}
//	
//	//UC5....
//	@Test
//	public void givenDateRange_WhenEmployeeDataRetrieved_ShouldMatch() throws EmployeePayrollException {
//		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
//		LocalDate startDate=LocalDate.of(2018, 01, 01);
//		LocalDate endDate=LocalDate.now();
//		List<EmployeePayrollData> employeePayrollDataList=employeePayrollService.getEmployeeWithDateRange(startDate,endDate);
//		assertEquals(3,employeePayrollDataList.size());
//	}
	//UC6...
	@Test
	public void givenEmployeeData_WhenSumSalaryWithGender_ShouldMatch() throws EmployeePayrollException {
		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String,Double> salarySumByGender=employeePayrollService.getEmployeeSalarySumGroupWithGender();
		assertTrue(salarySumByGender.get("F").equals(600000.0) && salarySumByGender.get("M").equals(100000.0));
	}
	
	//UC7..
////	@Test
//	public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() throws EmployeePayrollException{
//		EmployeePayrollService employeePayrollService=new EmployeePayrollService();
//		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
//		employeePayrollService.addEmployeeToPayroll("Mark",500000.00,LocalDate.now(),"M",2);
//		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
//		assertTrue(result);
//	}

}
